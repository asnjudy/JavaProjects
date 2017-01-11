package luceneinaction.chapter5.query;

import luceneinaction.common.TestUtil;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by asnju on 2017/1/9.
 */
public class SpanTermQueryTest {


    static Directory directory = new RAMDirectory();
    static IndexWriter writer;
    static IndexSearcher searcher;

    public static void index() throws IOException {
        writer = new IndexWriter(directory, new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);

        Document doc = new Document();
        doc.add(new Field("f", "the quick brown fox jumps over the lazy dog", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);

        doc = new Document();
        doc.add(new Field("f", "the quick red fox jumps over the sleepy cat", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);

        writer.close();
    }


    public static void search(Query query) throws IOException {
        System.out.println(query);

        searcher = new IndexSearcher(directory);
        TopDocs hits = searcher.search(query, 10);
        System.out.println("[search] match count: " + hits.totalHits);
        TestUtil.printTopDocs(searcher, hits);

        searcher.close();
    }


    public static void spanSearch(SpanQuery query) throws IOException {
        System.out.println(query);

        searcher = new IndexSearcher(directory);
        TopDocs hits = searcher.search(query, 10);
        float[] scores = new float[2];

        for (ScoreDoc sd: hits.scoreDocs) {
            scores[sd.doc] = sd.score;
        }


        IndexReader reader = searcher.getIndexReader();
        Spans spans = query.getSpans(reader);


        int numSpans = 0;

        while (spans.next()) {
            numSpans++;
            int id = spans.doc(); // 取得当前文档的ID
            Document doc = reader.document(id);

            TokenStream stream = new WhitespaceAnalyzer().tokenStream("contents", new StringReader(doc.get("f")));
            TermAttribute termAttribute = stream.addAttribute(TermAttribute.class);

            StringBuilder buffer = new StringBuilder();
            buffer.append("    ");
            int i = 0;
            while (stream.incrementToken()) {
                if (i == spans.start())
                    buffer.append("<");

                buffer.append(termAttribute.term());

                if (i + 1 == spans.end())
                    buffer.append(">");

                buffer.append(" ");
                i++;
            }
            buffer.append("(").append(scores[id]).append(")");
            System.out.println(buffer);
        } // end-while

        if (numSpans == 0)
            System.out.println("    No spans");
        System.out.println();

        searcher.close();
    }


    public static void test() throws IOException {
        index();

        SpanTermQuery brown = new SpanTermQuery(new Term("f", "brown"));
        search(brown);
    }

    public static void testSpanSearch() throws IOException {
        index();

        SpanTermQuery brown = new SpanTermQuery(new Term("f", "brown")); // f:brown
        SpanFirstQuery query = new SpanFirstQuery(brown, 3); // spanFirst(f:brown, 2)

        spanSearch(query);
    }


    public static void testSpanNearQuery() throws IOException {
        index();

        SpanTermQuery quick = new SpanTermQuery(new Term("f", "quick"));
        SpanTermQuery brown = new SpanTermQuery(new Term("f", "brown"));
        SpanTermQuery dog = new SpanTermQuery(new Term("f", "dog"));

        SpanQuery[] quick_brown_dog = new SpanQuery[] {quick, brown, dog};
        SpanNearQuery spanNearQuery = new SpanNearQuery(quick_brown_dog, 0, true); //spanNear([f:quick, f:brown, f:dog], 0, true)
        spanSearch(spanNearQuery);

        /* "the quick brown fox jumps over the lazy dog" */
        spanNearQuery = new SpanNearQuery(quick_brown_dog, 4, true);
        spanSearch(spanNearQuery);


        spanNearQuery = new SpanNearQuery(quick_brown_dog, 5, true); //spanNear([f:quick, f:brown, f:dog], 5, true)
        /**
         * spanNear([f:quick, f:brown, f:dog], 5, true)
         *      the <quick brown fox jumps over the lazy dog> (0.27026406)
         */
        spanSearch(spanNearQuery);

        SpanTermQuery lazy = new SpanTermQuery(new Term("f", "lazy"));
        SpanTermQuery fox = new SpanTermQuery(new Term("f", "fox"));
        spanNearQuery = new SpanNearQuery(new SpanQuery[] {lazy, fox}, 3, false);
        spanSearch(spanNearQuery);


        PhraseQuery phraseQuery = new PhraseQuery();
        phraseQuery.add(new Term("f", "lazy"));
        phraseQuery.add(new Term("f", "fox"));
        phraseQuery.setSlop(4);  // f:"lazy fox"~4
        search(phraseQuery);

        phraseQuery.setSlop(5); //f:"lazy fox"~5
        /**
         * [search] match count: 1
         Document<stored,indexed,tokenized<f:the quick brown fox jumps over the lazy dog>>
         */
        search(phraseQuery);
    }

    public static void main(String[] args) throws IOException {

        test();
        testSpanSearch();
        testSpanNearQuery();
    }
}
