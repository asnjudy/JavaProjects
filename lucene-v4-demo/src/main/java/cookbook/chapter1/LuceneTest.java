package cookbook.chapter1;

import org.apache.lucene.analysis.Analyzer;
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
 * Created by xwt on 2016/7/17.
 */
public class LuceneTest {

    private String searchTerm;


    public static void main(String[] args) throws IOException, ParseException {
        Analyzer analyzer = new StandardAnalyzer(); //initialize an analyzer
        Directory directory = new RAMDirectory();     //initialize directory
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer); //initialize IndexWriterConfig
        IndexWriter writer = new IndexWriter(directory, config); //obtain an IndexWriter, to put a piece of content into the index


        Document doc = new Document();
        String text = "Lucene is an Information Retrieval library written in Java";
        doc.add(new TextField("Content", text, Field.Store.YES));

        writer.addDocument(doc);
        writer.close(); //call this method to close the index and commit all changes

        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("Content", analyzer);
        Query query = parser.parse("Lucene");

        int hitsPerPage = 10;
        TopDocs docs = searcher.search(query, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        int end = Math.min(docs.totalHits, hitsPerPage);
        System.out.println("Total Hits: " + docs.totalHits);
        System.out.println("Results: ");
        for (int i = 0; i < end; i++) {
            Document d = searcher.doc(hits[i].doc);
            System.out.println("Content: " + d.get("Content"));
        }

    }
}
