package luceneinaction.chapter5.function;

import luceneinaction.common.TestUtil;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.function.CustomScoreProvider;
import org.apache.lucene.search.function.CustomScoreQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.Date;

/**
 * Created by asnju on 2017/1/10.
 */
public class RecencyBootingQuery extends CustomScoreQuery {

    double multiplier;
    int today;
    int maxDaysAgo;
    static int MSEC_PER_DAY = 1000*3600*24;
    String dayField;



    public RecencyBootingQuery(Query subQuery, double multiplier, int maxDaysAgo, String dayField) {
        super(subQuery);
        this.today = (int) (new Date().getTime() / MSEC_PER_DAY);
        this.multiplier = multiplier;
        this.dayField = dayField;
    }


    private class RecencyBooster extends CustomScoreProvider {

        final int[] publishDay;

        /**
         * Creates a new instance of the provider class for the given {@link IndexReader}.
         *
         * @param reader
         */
        public RecencyBooster(IndexReader reader) throws IOException {
            super(reader);
            publishDay = FieldCache.DEFAULT.getInts(reader, dayField);
        }

        public float customScore(int doc, float subQueryScore, float valSrcScore) {
            int daysAgo = today - publishDay[doc];
            if (daysAgo < maxDaysAgo) {
                float boost = (float) (multiplier * (maxDaysAgo - daysAgo) / maxDaysAgo);
                return (float) (subQueryScore * (1.0 + boost));
            } else {
                return subQueryScore;
            }
        }
    }

    public CustomScoreProvider getCustomScoreProvider(IndexReader reader) throws IOException {
        return new RecencyBooster(reader);
    }
    



    public static void main(String[] args) throws IOException, ParseException {

        Directory directory = TestUtil.getBookIndexDirectory();
        IndexReader reader = IndexReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setDefaultFieldSortScoring(true, true);

        QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new StandardAnalyzer(Version.LUCENE_30));
        Query query = parser.parse("java in action");
        Query query2 = new RecencyBootingQuery(query, 2.0, 2*365, "pubmonthAsDay");

        System.out.println(query);
        System.out.println(query2);

        Sort sort = new Sort(new SortField[] {
                SortField.FIELD_SCORE,
                new SortField("title2", SortField.STRING)
        });

        TopDocs hits = searcher.search(query2, null, 5, sort);

        for (ScoreDoc scoreDoc : hits.scoreDocs) {

            Document doc = reader.document(scoreDoc.doc);


            System.out.println("Title: " + doc.get("title")
                    + "\tpubmonth: " + doc.get("pubmonth")
                    + "\tscore: " + scoreDoc.score);
        }

        searcher.close();
        reader.close();
        directory.close();
    }
}
