package luceneinaction.chapter6.collector;

import luceneinaction.common.TestUtil;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by asnju on 2017/1/11.
 */
public class BookLinkCollectorTest {


    public static void testCollecting() throws IOException {
        Directory directory = TestUtil.getBookIndexDirectory();
        TermQuery query = new TermQuery(new Term("contents", "junit"));
        IndexSearcher searcher = new IndexSearcher(directory);

        BookLinkCollector collector = new BookLinkCollector();
        searcher.search(query, collector);

        Map<String, String> linkMap = collector.getLinks();

        Set<Map.Entry<String, String>> entries = linkMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }



        searcher.close();
        directory.close();
    }

    public static void main(String[] args) throws IOException {

        testCollecting();
    }
}
