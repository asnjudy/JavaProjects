package luceneinaction.common;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Created by asnju on 2016/9/15.
 */
public class TestUtil {

    private static final String indexDir = "index/books";

    public static Directory getBookIndexDirectory() throws IOException {
        return FSDirectory.open(new File(indexDir));
    }
}
