package luceneinaction.chapter6;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;
import org.apache.lucene.search.SortField;

import java.io.IOException;

/**
 * Created by asnju on 2017/1/11.
 */
public class DistanceComparatorSource extends FieldComparatorSource {


    private int x;
    private int y;

    public DistanceComparatorSource(int x, int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public FieldComparator newComparator(String fieldname, int numHits, int sortPos, boolean reversed) throws IOException {
        return new DistanceScoreDocLookupComparator(fieldname, numHits);
    }


    /**
     * 内部类，用于比较距离
     */
    private class DistanceScoreDocLookupComparator extends FieldComparator {

        private int[] xDoc, yDoc;
        private float[] values;
        private float bottom;
        String fieldName;

        public DistanceScoreDocLookupComparator(String fieldName, int numHits) {
            values = new float[numHits];
            this.fieldName = fieldName;
        }

        // 计算2篇文档的距离
        private float getDistence(int doc) {
            int delta_x = xDoc[doc] - x;
            int delta_y = yDoc[doc] - y;
            return (float) Math.sqrt(delta_x * delta_x + delta_y * delta_y);
        }

        public int sortType() {
            return SortField.CUSTOM;
        }

        public String toString() {
            return "Distance from (" + x + ", " + y + ")";
        }

        @Override
        public int compare(int slot1, int slot2) {
            if (values[slot1] < values[slot2])
                return -1;
            if (values[slot1] > values[slot2])
                return 1;
            return 0;
        }

        @Override
        public void setBottom(int slot) {
            bottom = values[slot];
        }

        @Override
        public int compareBottom(int doc) throws IOException {
            float docDistance = getDistence(doc);
            if (bottom < docDistance)
                return -1;
            if (bottom > docDistance)
                return 1;
            return 0;
        }

        @Override
        public void copy(int slot, int doc) throws IOException {
            values[slot] = getDistence(doc);
        }

        @Override
        public void setNextReader(IndexReader reader, int docBase) throws IOException {
            xDoc = FieldCache.DEFAULT.getInts(reader, "x");
            yDoc = FieldCache.DEFAULT.getInts(reader, "y");
        }

        @Override
        public Comparable<?> value(int slot) {
            return new Float(values[slot]);
        }
    }
}
