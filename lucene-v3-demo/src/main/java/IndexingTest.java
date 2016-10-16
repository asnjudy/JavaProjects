import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class IndexingTest {


    protected String[] ids = {"1", "2"};
    protected String[] unindexed = {"Netherlands", "Italy"};
    protected String[] unstored = {"Amsterdam has lots of bridges", "Venice has lots of canals"};

    protected String[] text = {"Amsterdam", "Venice"};


    private Directory directory;

    public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException {
        IndexingTest test = new IndexingTest();
        test.createIndex();
        System.out.println(test.getHitCount("content", "has"));
    }

    public void createIndex() throws CorruptIndexException, LockObtainFailedException, IOException {

        directory = new RAMDirectory();

        IndexWriter writer = new IndexWriter(directory,
                new WhitespaceAnalyzer(),
                IndexWriter.MaxFieldLength.UNLIMITED);
        for (int i = 0; i < ids.length; i++) {
            Document document = new Document();
            document.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
            document.add(new Field("country", unindexed[i], Field.Store.YES, Field.Index.NO));
            document.add(new Field("content", unstored[i], Field.Store.NO, Field.Index.ANALYZED));
            document.add(new Field("city", text[i], Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(document);
        }
        writer.close();
    }

    protected int getHitCount(String fieldName, String searchString) throws IOException {

        IndexSearcher searcher = new IndexSearcher(directory);
        Term t = new Term(fieldName, searchString);

        Query query = new TermQuery(t);

        TopDocs topDocs = searcher.search(query, 10);
        int hitCount = topDocs.totalHits;

        searcher.close();
        return hitCount;

    }
}
