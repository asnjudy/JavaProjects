package luceneinaction.chapter5.query;

import luceneinaction.common.TestUtil;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by asnju on 2017/1/9.
 *
 * MultiFieldQueryParser类使用
 */
public class MultiFieldQueryParserExample {


    public static void testDefaultOperator() throws ParseException, IOException {

        // title:development subject:development
        QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30, new String[] {"title", "subject" }, new SimpleAnalyzer());
        Query query = parser.parse("development");

        Directory directory = TestUtil.getBookIndexDirectory();

        IndexSearcher searcher = new IndexSearcher(directory, true);
        TopDocs hits = searcher.search(query, 10);

        System.out.println(query);
        TestUtil.printTopDocs(searcher, hits);

    }


    public static void testSpecifiedOperator() throws ParseException, IOException {

        // +title:lucene +subject:lucene
        Query query = MultiFieldQueryParser.parse(Version.LUCENE_30,
                "lucene",
                new String[] {"title", "subject"},
                new BooleanClause.Occur[] {BooleanClause.Occur.MUST, BooleanClause.Occur.MUST},
                new SimpleAnalyzer());

        Directory directory = TestUtil.getBookIndexDirectory();
        IndexSearcher searcher = new IndexSearcher(directory, true);

        TopDocs hits = searcher.search(query, 10);

        System.out.println(query);
        TestUtil.printTopDocs(searcher, hits);
    }


    public static void main(String[] args) throws IOException, ParseException {

        testDefaultOperator();
    }
}
