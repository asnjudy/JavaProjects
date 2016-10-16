package cookbook.chapter3.similarity;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/15.
 */
public class MySimilarityTest {

    public static void main(String[] args) throws IOException {

        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);

        MySimilarity mySim = new MySimilarity(new DefaultSimilarity());
        config.setSimilarity(mySim);

        IndexWriter indexWriter = new IndexWriter(directory, config);

        Document document = new Document();
        TextField textField = new TextField("name", "", Field.Store.YES);
        NumericDocValuesField docValuesField = new NumericDocValuesField("ranking", 1);

        long ranking = 1L;

        String[] names = {"John R Smith", "Mary Smith", "Peter Smith"};

        for (String name : names) {
            ranking *= 2;
            textField.setStringValue(name);
            docValuesField.setLongValue(ranking);
            document.removeField("name");
            document.removeField("ranking");
            document.add(textField);
            document.add(docValuesField);

            indexWriter.addDocument(document);
        }
        indexWriter.commit();

        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        indexSearcher.setSimilarity(mySim);
        Query query = new TermQuery(new Term("name", "smith"));
        TopDocs topDocs = indexSearcher.search(query, 100);
        System.out.println("searching 'smith'");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            document = indexReader.document(scoreDoc.doc);
            System.out.println(document.getField("name").stringValue());
        }
    }
}
