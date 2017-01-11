package luceneinaction.common;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 * Created by asnju on 2017/1/9.
 */
public class SearchUtils {



    public static void displayResults2(Query query, Sort sort) throws IOException {
        IndexSearcher searcher = new IndexSearcher(TestUtil.getBookIndexDirectory());
        searcher.setDefaultFieldSortScoring(true, false);

        TopDocs results = searcher.search(query, null, 20, sort);

        System.out.println("\nResults for: " + query.toString() + " sorted by " + sort);
        System.out.println(StringUtils.rightPad("Title", 30)
                + StringUtils.rightPad("pubmonth", 10)
                + StringUtils.center("id", 4)
                + StringUtils.center("score", 15));


        for (ScoreDoc sd : results.scoreDocs) {
            int docID = sd.doc;
            float score = sd.score;
            Document doc = searcher.doc(docID);
            System.out.println("Title: " + doc.get("title"));
            System.out.println("pubmonth" + doc.get("pubmonth"));
            System.out.println("score: " + score);
            System.out.println("category: " + doc.get("category"));
           // System.out.println("explain: " + searcher.explain(query, docID));
        }
    }

    public static void displayResults(Query query, Sort sort) throws IOException {
        IndexSearcher indexSearcher = new IndexSearcher(TestUtil.getBookIndexDirectory());
        indexSearcher.setDefaultFieldSortScoring(true, false);

        /**
         * search(Query query, Filter filter, int n, Sort sort): TopFieldDocs
         */
        TopDocs topDocs = indexSearcher.search(query, null, 20, sort);
        System.out.println("Results for: " + query.toString() + " sorted by " + sort);

        System.out.println(StringUtils.rightPad("Title", 30)
                + StringUtils.rightPad("pubmonth", 10)
                + StringUtils.center("id", 4)
                + StringUtils.center("score", 15));

        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        DecimalFormat formatter = new DecimalFormat("0.######");

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int docID = scoreDoc.doc;
            float score = scoreDoc.score;
            Document document = indexSearcher.doc(docID);

            System.out.print(
                    StringUtils.rightPad(StringUtils.abbreviate(document.get("title"), 29), 30)
                            + StringUtils.rightPad(document.get("pubmonth"), 10)
                            + StringUtils.center("" + docID, 4)
                            + StringUtils.leftPad(formatter.format(score), 12));

            out.println("    " + document.get("category"));
            //out.println(indexSearcher.explain(query, docID));
        }

        indexSearcher.close();
    }
}
