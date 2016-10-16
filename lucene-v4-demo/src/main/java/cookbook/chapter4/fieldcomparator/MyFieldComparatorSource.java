package cookbook.chapter4.fieldcomparator;

import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/16.
 */
public class MyFieldComparatorSource extends FieldComparatorSource {
    @Override
    public FieldComparator<?> newComparator(String fieldname, int numHits, int sortPos, boolean reversed) throws IOException {
        return new MyFieldComparator(fieldname, numHits);
    }
}
