package luceneinaction.chapter6.collector;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Scorer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asnju on 2017/1/11.
 */
public class BookLinkCollector extends Collector {

    private Map<String, String> documents = new HashMap<>();
    private Scorer scorer;
    private String[] urls;
    private String[] titles;


    public Map<String, String> getLinks() {
        return Collections.unmodifiableMap(documents);
    }


    @Override
    public void setScorer(Scorer scorer) throws IOException {
        this.scorer = scorer;
    }

    @Override
    public void collect(int docID) throws IOException {
        try {
            String url = urls[docID];
            String title = titles[docID];
            documents.put(url, title);
            System.out.println(title + ": " + scorer.score());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setNextReader(IndexReader reader, int docBase) throws IOException {
        urls = FieldCache.DEFAULT.getStrings(reader, "url");
        titles = FieldCache.DEFAULT.getStrings(reader, "title2");
    }

    @Override
    public boolean acceptsDocsOutOfOrder() {
        return true; // 允许无次序的文档ID
    }
}
