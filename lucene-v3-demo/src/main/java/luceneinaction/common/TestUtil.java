package luceneinaction.common;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by asnju on 2016/9/15.
 */
public class TestUtil {

    private static final String indexDir = "index/books2";

    public static Directory getBookIndexDirectory() throws IOException {
        return FSDirectory.open(new File(indexDir));
    }


    /**
     * 简单打印搜索结果
     * @param searcher
     * @param topDocs
     * @throws IOException
     */
    public static void printTopDocs(IndexSearcher searcher, TopDocs topDocs) throws IOException {
        System.out.println("match count: " + topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int docID = scoreDoc.doc;
            float score = scoreDoc.score;
            Document document = searcher.doc(docID);
            System.out.println(document);
        }
    }


    public static void printTopDocsPretty(IndexSearcher searcher, TopDocs topDocs) throws IOException {
        System.out.println("/************************************");
        System.out.println("match count: " + topDocs.totalHits + "\n");

        StringBuffer buffer = new StringBuffer();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int docID = scoreDoc.doc;
            float score = scoreDoc.score;
            Document doc = searcher.doc(docID);
            List<Fieldable> fields = doc.getFields();
            for (Fieldable field : fields) {
                System.out.println(field.name() + ": " + field.stringValue());
            }
            System.out.println("score: " + score + "\n");
        }
        System.out.println("***********************************/");

    }
}
