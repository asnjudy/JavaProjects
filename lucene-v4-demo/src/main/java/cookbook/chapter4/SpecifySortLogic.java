package cookbook.chapter4;

import cookbook.chapter4.fieldcomparator.MyFieldComparatorSource;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/16.
 */
public class SpecifySortLogic {

    public static void main(String[] args) throws IOException {

        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        Document document = new Document();


        StringField stringField = new StringField("name", "", Field.Store.YES);
        String[] contents = {"foxtrot", "echo", "delta", "charlie", "bravo", "alpha"};

        for (String content : contents) {
            stringField.setStringValue(content);
            document.removeField("name");
            document.add(stringField);
            indexWriter.addDocument(document);
        }
        indexWriter.commit();


        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        WildcardQuery query = new WildcardQuery(new Term("name", "*"));

        SortField sortField2 = new SortField("name", SortField.Type.STRING);

        SortField sortField = new SortField("name", new MyFieldComparatorSource());
        Sort sort = new Sort(sortField);

        TopDocs topDocs = indexSearcher.search(query, null, 100, sort);

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            document = indexReader.document(scoreDoc.doc);
            System.out.println(scoreDoc.score + ": " + document.getField("name").stringValue());
        }


    }
}
