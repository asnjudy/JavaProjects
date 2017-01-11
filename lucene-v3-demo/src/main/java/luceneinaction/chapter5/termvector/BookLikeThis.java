package luceneinaction.chapter5.termvector;

import com.sun.org.apache.xpath.internal.operations.Bool;
import luceneinaction.common.TestUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import javax.print.Doc;
import java.io.IOException;

/**
 * Created by asnju on 2017/1/10.
 *
 * 词向量
 *
 Document<
 isbn		stored,indexed<isbn:193239480X>
 category	stored,indexed<category:/technology/computers/programming>
 title		stored,indexed,tokenized,termVector,termVectorOffsets,termVectorPosition<title:Ant in Action>
 title2		stored,indexed,tokenized,termVector,termVectorOffsets,termVectorPosition<title2:ant in action>
 author		stored,indexed,termVector,termVectorOffsets,termVectorPosition<author:Steve Loughran>
 author		stored,indexed,termVector,termVectorOffsets,termVectorPosition<author:Erik Hatcher>
 url		stored,indexed,omitNorms<url:http://www.manning.com/loughran>
 subject	stored,indexed,tokenized,termVector,termVectorOffsets,termVectorPosition<subject:apache ant build tool junit java development>
 pubmonth	stored,indexed,tokenized,omitNorms,omitTermFreqAndPositions<pubmonth:200707>>
 */
public class BookLikeThis {


    IndexReader reader;
    IndexSearcher searcher;

    public BookLikeThis(IndexReader reader) {
        this.reader = reader;
        searcher = new IndexSearcher(reader);
    }


    public Document[] docsLike(int id, int max) throws IOException {
        Document doc = reader.document(id);

        String[] authors = doc.getValues("author");
        BooleanQuery authorQuery = new BooleanQuery();

        for (String author: authors) {
            authorQuery.add(new TermQuery(new Term("author", author)), BooleanClause.Occur.SHOULD);
        }
        authorQuery.setBoost(2.0f);

        TermFreqVector vector = reader.getTermFreqVector(id, "subject");
        BooleanQuery subjectQuery = new BooleanQuery();
        for (String vecTerm : vector.getTerms()) {
            TermQuery termQuery = new TermQuery(new Term("subject", vecTerm));
            subjectQuery.add(termQuery, BooleanClause.Occur.SHOULD);
        }

        BooleanQuery likeThisQuery = new BooleanQuery();
        likeThisQuery.add(authorQuery, BooleanClause.Occur.SHOULD);
        likeThisQuery.add(subjectQuery, BooleanClause.Occur.SHOULD);
        likeThisQuery.add(new TermQuery(new Term("isbn", doc.get("isbn"))), BooleanClause.Occur.MUST_NOT);


        System.out.println("likeThisQuery: " + likeThisQuery);
        TopDocs hits = searcher.search(likeThisQuery, 10);
        int size = max;
        if (max > hits.scoreDocs.length)
            size = hits.scoreDocs.length;

        Document[] docs = new Document[size];
        for (int i = 0; i < size; i++)
            docs[i] = reader.document(hits.scoreDocs[i].doc);

        return docs;
    }

    public static void main(String[] args) throws IOException {

        Directory directory = TestUtil.getBookIndexDirectory();
        IndexReader reader = IndexReader.open(directory);

        int numDocs = reader.maxDoc();

        BookLikeThis blt = new BookLikeThis(reader);

        for (int i = 0; i < numDocs; i++) {
            System.out.println();
            Document doc = reader.document(i);
            System.out.println(doc.get("title"));

            Document[] docs = blt.docsLike(i, 10);
            if (docs.length == 0)
                System.out.println("   None like this");

            for (Document likeThisDoc : docs)
                System.out.println("   -> " + likeThisDoc.get("title"));
        }
    }
}
