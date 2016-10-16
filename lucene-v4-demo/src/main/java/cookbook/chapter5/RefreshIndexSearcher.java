package cookbook.chapter5;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import java.io.IOException;

/**
 * Created by asnju on 2016/10/16.
 */
public class RefreshIndexSearcher {

    private Directory directory = new RAMDirectory();
    private Analyzer analyzer = new StandardAnalyzer();
    private Document document = new Document();

    private IndexWriter indexWriter = null;

    private SearcherManager searcherManager = null;
    private QueryParser queryParser = null;
    private IndexSearcher indexSearcher = null;


    public RefreshIndexSearcher() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        indexWriter = new IndexWriter(directory, config);

        searcherManager = new SearcherManager(indexWriter, true, new SearcherFactory());
        queryParser = new QueryParser("content", analyzer);
    }


    public void saveToIndex(String content) throws IOException {
        TextField textField = new TextField("content", "", Field.Store.YES);
        textField.setStringValue(content);
        document.removeField("content");
        document.add(textField);

        indexWriter.addDocument(document);
    }


    public void search(String str) throws IOException, ParseException {
        // another search operation
        searcherManager.maybeRefresh();
        indexSearcher = searcherManager.acquire();

        Query query = queryParser.parse(str);
        TopDocs topDocs = indexSearcher.search(query, 100);


        System.out.println("+++++++++ " + topDocs.totalHits + " +++++++++++++++");

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(scoreDoc.score + ": " + document.getField("content").stringValue());
        }
        searcherManager.release(indexSearcher);
        indexWriter.commit();
    }


    public static void main(String[] args) throws IOException, ParseException {


        RefreshIndexSearcher demo = new RefreshIndexSearcher();


        String[] contents = {
                "Dumpty sat on a wall",
                "Humpty Dumpty had a great wall",
                "All the king's horses and all the king's men",
                "Could't put Humpty together again"
        };

        for (String content : contents) {
            demo.saveToIndex(content);
        }

        demo.search("humpty dumpty");

        // add more documents to index here
        demo.saveToIndex("general add another documents asnjudy");

        demo.search("asnjudy");
    }
}
