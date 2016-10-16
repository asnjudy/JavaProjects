package cookbook.chapter3;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/15.
 */
public class FiledNorms {

    public static void main(String[] args) throws IOException {

        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        Document document = new Document();

        TextField textField = new TextField("name", "", Field.Store.YES);

        float boost = 1f;
        String[] names = {"John R Smith", "Mary Smith", "Peter Smith"};

        for (String name : names) {
            boost *= 1.1;
            textField.setStringValue(name);
            textField.setBoost(boost);
            document.removeField("name");
            document.add(textField);
            indexWriter.addDocument(document);
        }
        indexWriter.commit(); //提交Doc到倒排索引表

        // 查询Doc
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Query query = new TermQuery(new Term("name", "smith"));
        TopDocs topDocs = indexSearcher.search(query, 100);
        System.out.println("Searching 'smith'");

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            document = indexReader.document(scoreDoc.doc);
            System.out.println(document.getField("name").stringValue());
        }
    }
}
