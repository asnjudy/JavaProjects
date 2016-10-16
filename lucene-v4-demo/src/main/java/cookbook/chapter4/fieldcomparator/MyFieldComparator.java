package cookbook.chapter4.fieldcomparator;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldComparator;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/16.
 */
public class MyFieldComparator extends FieldComparator<String> {


    private String field;
    private String bottom;
    private String topValue;
    private BinaryDocValues cache;
    private String[] values;

    public MyFieldComparator(String field, int numHits) {
        this.field = field;
        this.values = new String[numHits];
    }


    @Override
    public void setBottom(int slot) {
        this.bottom = values[slot];
    }

    @Override
    public void setTopValue(String value) {
        this.topValue = value;
    }



    @Override
    public int compare(int slot1, int slot2) {
        return compareValues(values[slot1], values[slot2]);
    }


    @Override
    public int compareBottom(int doc) throws IOException {
        return compareValues(bottom, cache.get(doc).utf8ToString());
    }

    @Override
    public int compareTop(int doc) throws IOException {
        return compareValues(topValue, cache.get(doc).utf8ToString());
    }

    public int compareValues(String first, String second) {
        int val = first.length() - second.length();
        return val == 0 ? second.compareTo(first) : val;
    }

    @Override
    public void copy(int slot, int doc) throws IOException {
        values[slot] = cache.get(doc).utf8ToString();
    }

    @Override
    public FieldComparator<String> setNextReader(AtomicReaderContext context) throws IOException {

        this.cache = FieldCache.DEFAULT.getTerms(context.reader(), field, true);

        return this;
    }

    @Override
    public String value(int slot) {
        return values[slot];
    }
}
