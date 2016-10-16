package cookbook.chapter5;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TrackingIndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/17.
 */
public class TrackingIndexWriterTest {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        SearcherManager searcherManager = new SearcherManager(indexWriter, true, new SearcherFactory());

        TrackingIndexWriter trackingIndexWriter = new TrackingIndexWriter(indexWriter);

        ControlledRealTimeReopenThread controlledRealTimeReopenThread = new ControlledRealTimeReopenThread(trackingIndexWriter, searcherManager, 5, 0.001f);

        controlledRealTimeReopenThread.start();

        long indexGeneration = 0;


        // add documents to index here
        String[] contents = {
                "Dumpty sat on a wall",
                "Humpty Dumpty had a great wall",
                "All the king's horses and all the king's men",
                "Could't put Humpty together again"
        };

        Document document = new Document();


        TextField textField = new TextField("content", "", Field.Store.YES);

        textField.setStringValue(contents[0]);
        document.removeField("content");
        document.add(textField);
        indexWriter.addDocument(document);

        textField.setStringValue(contents[1]);
        document.removeField("content");
        document.add(textField);
        indexGeneration = trackingIndexWriter.addDocument(document);

        controlledRealTimeReopenThread.waitForGeneration(indexGeneration);
        IndexSearcher indexSearcher = searcherManager.acquire();


        // perform search
        QueryParser queryParser = new QueryParser("content", analyzer);
        Query query = queryParser.parse("great");

        TopDocs topDocs = indexSearcher.search(query, 100);
        System.out.println("+++++++++ " + topDocs.totalHits + " +++++++++++++++");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(scoreDoc.score + ": " + document.getField("content").stringValue());
        }

        searcherManager.release(indexSearcher);
        indexWriter.commit();



        // add more documents to index
        textField.setStringValue(contents[2]);
        document.removeField("content");
        document.add(textField);
        indexGeneration = trackingIndexWriter.addDocument(document);

        controlledRealTimeReopenThread.waitForGeneration(indexGeneration);
        indexSearcher = searcherManager.acquire();

        // perform another search
        query = queryParser.parse("king's");
        topDocs = indexSearcher.search(query, 100);
        System.out.println("+++++++++ " + topDocs.totalHits + " +++++++++++++++");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(scoreDoc.score + ": " + document.getField("content").stringValue());
        }

        searcherManager.release(indexSearcher);
        indexWriter.commit();
        searcherManager.release(indexSearcher);
        controlledRealTimeReopenThread.close();
        indexWriter.commit();

    }
}
