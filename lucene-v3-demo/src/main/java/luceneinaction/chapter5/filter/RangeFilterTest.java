package luceneinaction.chapter5.filter;

import luceneinaction.common.TestUtil;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;

import java.io.IOException;

/**
 * Created by asnju on 2017/1/9.
 */
public class RangeFilterTest {


    static Query allBooks = new MatchAllDocsQuery();
    static IndexSearcher searcher;



    public static void testBooleanQuery() throws IOException {
        TermQuery categoryQuery = new TermQuery(new Term("category", "/philosophy/eastern"));
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(allBooks, BooleanClause.Occur.MUST);
        booleanQuery.add(categoryQuery, BooleanClause.Occur.MUST); // +*:* +category:/philosophy/eastern

        System.out.println(booleanQuery);
        TopDocs hits = searcher.search(booleanQuery, 10);
        TestUtil.printTopDocs(searcher, hits);

    }

    public static void searchWithFilter(Filter filter) throws IOException  {
        System.out.println("filter: " + filter);
        TopDocs hits = searcher.search(allBooks,filter, 10);
        TestUtil.printTopDocs(searcher, hits);
    }


    public static void main(String[] args) throws IOException {
        System.out.println(allBooks);

        searcher = new IndexSearcher(TestUtil.getBookIndexDirectory());

        // title2域内容的首字母匹配
        Filter filter = new TermRangeFilter("title2", "a", "b", true, true); // title2:[a TO b]
        searchWithFilter(filter);


        filter = NumericRangeFilter.newIntRange("pubmonth", 201001, 201006, true, true); // pubmonth:[201001 TO 201006]
        searchWithFilter(filter);


        filter = FieldCacheRangeFilter.newStringRange("title2", "d", "j", true, true); // title2:[d TO j]
        searchWithFilter(filter);


        filter = new FieldCacheTermsFilter("category",
                new String[] {"/health/alternative/chinese",
                        "/technology/computers/ai",
                        "/technology/computers/programming"}); // org.apache.lucene.search.FieldCacheTermsFilter@7b3300e5
        searchWithFilter(filter);


        TermQuery category = new TermQuery(new Term("category", "/philosophy/eastern"));
        filter = new QueryWrapperFilter(category); // QueryWrapperFilter(category:/philosophy/eastern)
        searchWithFilter(filter);


        SpanQuery categorySpanQuery = new SpanTermQuery(new Term("category", "/philosophy/eastern"));
        filter = new SpanQueryFilter(categorySpanQuery); // SpanQueryFilter(category:/philosophy/eastern)
        searchWithFilter(filter);


        testBooleanQuery();

        searcher.close();
    }
}
