package luceneinaction.chapter6;

import luceneinaction.common.TestUtil;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * Created by asnju on 2017/1/11.
 */
public class DistanceSortingTest {

    private RAMDirectory directory = new RAMDirectory();
    private IndexSearcher searcher;

    public DistanceSortingTest() throws IOException {
        index();
        searcher = new IndexSearcher(directory);
    }




    public void index() throws IOException {
        IndexWriter writer = new IndexWriter(directory, new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);

        Document doc = new Document();
        doc.add(new Field("name", "EL Charro", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("type", "restaurant", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("location", 1 + "," + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
        writer.addDocument(doc);

        doc = new Document();
        doc.add(new Field("name", "Cafe Poca Cosa", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("type", "restaurant", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("location", 5 + "," + 9, Field.Store.YES, Field.Index.NOT_ANALYZED));
        writer.addDocument(doc);

        doc = new Document();
        doc.add(new Field("name", "Los Betos", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("type", "restaurant", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("location", 9 + "," + 6, Field.Store.YES, Field.Index.NOT_ANALYZED));
        writer.addDocument(doc);

        doc = new Document();
        doc.add(new Field("name", "Nico's Taco Shop", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("type", "restaurant", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("location", 3 + "," + 8, Field.Store.YES, Field.Index.NOT_ANALYZED));
        writer.addDocument(doc);

        writer.close();
    }


    /**
     * 分析排序过程
     *
     * @throws IOException
     */
    public void testNearestRestaurantToHome() throws IOException {
        Sort sort = new Sort(new SortField("location", new DistanceComparatorSource(0, 0)));
        Query query = new TermQuery(new Term("type", "restaurant"));

        TopDocs hits = searcher.search(query, null, 10, sort);
        TestUtil.printTopDocsPretty(searcher, hits);
    }

    public void testNearestRestaurantToWork() throws IOException {
        Sort sort = new Sort(new SortField("unused", new DistanceComparatorSource(10, 10)));
        Query query = new TermQuery(new Term("type", "restaurant"));

        TopFieldDocs fieldDocs = searcher.search(query, null, 10, sort);
        System.out.println(fieldDocs.totalHits); // 4
        System.out.println(fieldDocs.scoreDocs.length); // 4

        FieldDoc fieldDoc = (FieldDoc) fieldDocs.scoreDocs[0];
        System.out.println(fieldDoc.fields[0]); // 14.142136

        Document document = searcher.doc(fieldDoc.doc);
        System.out.println(document.get("name"));

        TestUtil.printTopDocsPretty(searcher, fieldDocs);

    }


    public static void main(String[] args) throws IOException {
        DistanceSortingTest test = new DistanceSortingTest();

        test.testNearestRestaurantToHome();
    }
}
