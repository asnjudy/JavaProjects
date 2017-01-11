package luceneinaction.chapter5.query;

import luceneinaction.chapter4.synonym.SynonymAnalyzer;
import luceneinaction.chapter4.synonym.SynonymEngine;
import luceneinaction.common.TestUtil;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by asnju on 2017/1/9.
 */
public class MultiPhraseQueryExample {

    static Directory directory = new RAMDirectory();
    static IndexWriter writer;
    static IndexSearcher searcher;


    public static void index() throws IOException {

        writer = new IndexWriter(directory, new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);

        Document doc = new Document();
        doc.add(new Field("field", "the quick brown fox jumped over the lazy dog", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);

        Document doc2 = new Document();
        doc2.add(new Field("field", "the fast fox hopped over the hound", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc2);

        writer.close();
    }

    public static void search() throws IOException {
        searcher = new IndexSearcher(directory);

        MultiPhraseQuery query = new MultiPhraseQuery();
        query.add(
                new Term[] {
                        new Term("field", "quick"),
                        new Term("field", "fast")
                }
        );
        query.add(new Term("field", "fox"));
        System.out.println(query);

        TopDocs hits = searcher.search(query, 10);
        System.out.println("fast fox match: " + hits.totalHits);
        TestUtil.printTopDocs(searcher, hits);

        query.setSlop(1);
        hits = searcher.search(query, 10);
        System.out.println("both match: " + hits.totalHits);
        TestUtil.printTopDocs(searcher, hits);

    }




    /**
     * 向短语位置动态插入同义词，这样就能获得更多的相关匹配
     * @throws ParseException
     */
    public static void testQueryParser() throws ParseException {
        SynonymEngine engine = new SynonymEngine() {
            @Override
            public String[] getSynonyms(String s) throws IOException {

                if (s.equals("quick"))
                    return new String[] {"fast"};
                else
                    return null;
            }
        };


        QueryParser parser = new QueryParser(Version.LUCENE_30, "field", new SynonymAnalyzer(engine));
        Query query = parser.parse("\"quick fox\"");

        System.out.println("query: " + query.toString());
        if (query instanceof MultiPhraseQuery)
            System.out.println("query is MultiPhraseQuery");
        else
            System.out.println("query is not MultiPhraseQuery");


    }


    public static void main(String[] args) throws IOException, ParseException {

        index();
        search();
    }

}
