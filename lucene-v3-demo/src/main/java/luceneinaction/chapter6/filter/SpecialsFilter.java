package luceneinaction.chapter6.filter;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

import java.io.IOException;

/**
 * Created by asnju on 2017/1/11.
 */
public class SpecialsFilter extends Filter {

    private SpecialsAccessor accessor;


    public SpecialsFilter(SpecialsAccessor accessor) {
        this.accessor = accessor;
    }



    @Override
    public DocIdSet getDocIdSet(IndexReader reader) throws IOException {

        OpenBitSet bits = new OpenBitSet(reader.maxDoc());

        String[] isbns = accessor.isbns(); // 获取当前书籍的isbn号

        int[] docs = new int[1];
        int[] freqs = new int[1];

        for (String isbn : isbns) {
            if (isbn != null) {
                TermDocs termDocs = reader.termDocs(new Term("isbn", isbn));
                int count = termDocs.read(docs, freqs);

                if (count == 1)
                    bits.set(docs[0]);
            }
        }

        return bits;
    }
}
