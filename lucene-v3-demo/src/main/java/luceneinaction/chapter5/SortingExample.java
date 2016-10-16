package luceneinaction.chapter5;

import luceneinaction.common.TestUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 * Created by asnju on 2016/9/15.
 */
public class SortingExample {

    private Directory directory;

    public SortingExample(Directory directory) {
        this.directory = directory;
    }

    public static void main(String[] args) throws ParseException, IOException {
        Query allBooks = new MatchAllDocsQuery();
        QueryParser queryParser = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));

        BooleanQuery query = new BooleanQuery();
        query.add(allBooks, BooleanClause.Occur.SHOULD);
        query.add(queryParser.parse("java OR action"), BooleanClause.Occur.SHOULD);

        Directory directory = TestUtil.getBookIndexDirectory();
        SortingExample example = new SortingExample(directory);
        example.displayResults(query, Sort.RELEVANCE);
        example.displayResults(query, Sort.INDEXORDER);
        example.displayResults(query, new Sort(new SortField("category", SortField.STRING)));
        example.displayResults(query, new Sort(new SortField("pubmonth", SortField.INT, true)));
        example.displayResults(query,
                new Sort(new SortField("category", SortField.STRING),
                        SortField.FIELD_SCORE,
                        new SortField("pubmonth", SortField.INT, true)
                ));

        example.displayResults(query,
                new Sort(new SortField[]{
                        SortField.FIELD_SCORE,
                        new SortField("category", SortField.STRING)}
                )
        );

        example.displayResults(allBooks, Sort.RELEVANCE);

        directory.close();
    }

    public void displayResults(Query query, Sort sort) throws IOException {
        IndexSearcher indexSearcher = new IndexSearcher(directory);
        indexSearcher.setDefaultFieldSortScoring(true, false);

        /**
         * search(Query query, Filter filter, int n, Sort sort): TopFieldDocs
         */
        TopDocs topDocs = indexSearcher.search(query, null, 20, sort);
        System.out.println("\nResults for: " + query.toString() + " sorted by " + sort);

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
