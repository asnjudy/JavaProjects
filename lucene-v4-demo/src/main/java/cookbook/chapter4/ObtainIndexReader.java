package cookbook.chapter4;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by asnju on 2016/10/15.
 */
public class ObtainIndexReader {

    public static void main(String[] args) throws IOException {

        Directory directory = FSDirectory.open(new File("index/chapter3"));

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        List<AtomicReaderContext> atomicReaderContexts = directoryReader.leaves();

        AtomicReader atomicReader = atomicReaderContexts.get(0).reader();

        DirectoryReader newDirectoryReader = DirectoryReader.openIfChanged(directoryReader);


        if (newDirectoryReader != null) {
            directoryReader.close();
        }
        newDirectoryReader.close();
    }
}
