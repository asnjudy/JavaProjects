package luceneinaction.chapter6.filter;

import luceneinaction.common.TestUtil;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

/**
 * Created by asnju on 2017/1/11.
 */
public class SpecialsFilterTest {


    public static void testCustomFilter() throws IOException {

        IndexSearcher searcher = new IndexSearcher(TestUtil.getBookIndexDirectory());

        String[] isbns = new String[] {"9780061142666", "9780394756820"};

        SpecialsAccessor accessor = new TestSpecialsAccessor(isbns);

        Filter filter = new SpecialsFilter(accessor);

        TopDocs hits = searcher.search(new MatchAllDocsQuery(), filter, 10);
        TestUtil.printTopDocsPretty(searcher, hits);

        searcher.close();
    }

    public static void main(String[] args) throws IOException {
        testCustomFilter();
    }
}
