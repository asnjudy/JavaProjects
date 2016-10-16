package cookbook.chapter6;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/17.
 */
public class FilterTest {


    public static void main(String[] args) throws IOException {

        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        Document doc = new Document();

        StringField stringField = new StringField("name", "", Field.Store.YES);
        TextField textField = new TextField("content", "", Field.Store.YES);
        IntField intField = new IntField("num", 0, Field.Store.YES);


        // First
        doc.removeField("name");
        doc.removeField("content");
        doc.removeField("num");

        stringField.setStringValue("First");
        textField.setStringValue("Humpty Dumpty sat on a wall,");
        intField.setIntValue(100);
        doc.add(stringField);
        doc.add(textField);
        doc.add(intField);
        indexWriter.addDocument(doc);

        // Second
        doc.removeField("name");
        doc.removeField("content");
        doc.removeField("num");

        stringField.setStringValue("Second");
        textField.setStringValue("Humpty Dumpty had a great fall.");
        intField.setIntValue(200);
        doc.add(stringField);
        doc.add(textField);
        doc.add(intField);
        indexWriter.addDocument(doc);


        // Third
        doc.removeField("name");
        doc.removeField("content");
        doc.removeField("num");

        stringField.setStringValue("Third");
        textField.setStringValue("All the king's horses and all the king's men");
        intField.setIntValue(300);
        doc.add(stringField);
        doc.add(textField);
        doc.add(intField);
        indexWriter.addDocument(doc);


        // Fourth
        doc.removeField("name");
        doc.removeField("content");
        doc.removeField("num");

        stringField.setStringValue("Fourth");
        textField.setStringValue("Couldn't put Humpty together again.");
        intField.setIntValue(400);
        doc.add(stringField);
        doc.add(textField);
        doc.add(intField);
        indexWriter.addDocument(doc);


        indexWriter.commit();
        indexWriter.close();

        // Perform search
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Query query = new TermQuery(new Term("content", "humpty"));

        NumericRangeFilter<Integer> filter = null;

        //filter = TermRangeFilter.newStringRange("name", "A", "G", true, true);
        filter = NumericRangeFilter.newIntRange("num", 20, 700, true, true);

        TopDocs topDocs = indexSearcher.search(query, filter, 100);
        System.out.println("---- search 'humpty' --- " + topDocs.toString() + " ----- ");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            doc = indexReader.document(scoreDoc.doc);
            System.out.println("name: " + doc.getField("name").stringValue()
                + " - content: " + doc.getField("content").stringValue()
                + " - num: " + doc.getField("num").stringValue()

            );
        }
        indexReader.close();

    }
}
