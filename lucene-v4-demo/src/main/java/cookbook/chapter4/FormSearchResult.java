package cookbook.chapter4;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/16.
 */
public class FormSearchResult {

    public static void main(String[] args) throws IOException, ParseException {

        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        Document document = new Document();

        TextField textField = new TextField("content", "", Field.Store.YES);

        String[] contents = {
                "Dumpty sat on a wall",
                "Dumpty had a great wall",
                "All the king's horses and all the king's men",
                "Could't put Humpty together again"
        };

        for (String content : contents) {
            textField.setStringValue(content);
            document.removeField("content");
            document.add(textField);
            indexWriter.addDocument(document);
        }
        indexWriter.commit();


        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        QueryParser queryParser = new QueryParser("content", analyzer);
        Query query = queryParser.parse("humpty dumpty");

        TopDocs topDocs = indexSearcher.search(query, 2);

        ScoreDoc lastScoreDoc = null;

        int page = 1;

        System.out.println("Total hits: " + topDocs.totalHits);

        while (true) {
            System.out.println("Page " + page);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                document = indexReader.document(scoreDoc.doc);
                System.out.println(scoreDoc.score + ":" + document.getField("content").stringValue());
                lastScoreDoc = scoreDoc;
            }

            topDocs = indexSearcher.searchAfter(lastScoreDoc, query, 2);

            if (topDocs.scoreDocs.length == 0) {
                break;
            }

            page++;
        }

    }
}
