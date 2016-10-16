package cookbook.chapter6;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCacheDocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/17.
 */
public class UserSecurityFilter extends Filter {

    private String userIdField;
    private String groupIdField;
    private String userId;
    private String groupId;

    public UserSecurityFilter(String userIdField, String groupIdField, String userId, String groupId) {
        this.userIdField = userIdField;
        this.groupIdField = groupIdField;
        this.userId = userId;
        this.groupId = groupId;
    }


    @Override
    public DocIdSet getDocIdSet(AtomicReaderContext context, Bits acceptDocs) throws IOException {

        final SortedDocValues userIdDocValues = FieldCache.DEFAULT.getTermsIndex(context.reader(), userIdField);
        final SortedDocValues groupIdDocValues = FieldCache.DEFAULT.getTermsIndex(context.reader(), groupIdField);

        final int userIdOrd = userIdDocValues.lookupTerm(new BytesRef(userId));
        final int groupIdOrd = groupIdDocValues.lookupTerm(new BytesRef(groupId));

        return new FieldCacheDocIdSet(context.reader().maxDoc(), acceptDocs) {
            @Override
            protected boolean matchDoc(int doc) {
                final int userIdDocOrd = userIdDocValues.getOrd(doc);
                final int groupIdDocOrd = groupIdDocValues.getOrd(doc);

                return userIdDocOrd == userIdOrd || groupIdDocOrd == groupIdOrd;
            }
        };


    }
}
