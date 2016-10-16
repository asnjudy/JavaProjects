package luceneinaction.chapter4.codec;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;


/**
 * Created by asnju on 2016/9/15.
 */
public class MetaphoneAnalyzerTest {

    public static void testKoolKat() throws IOException, ParseException {
        RAMDirectory directory = new RAMDirectory();
        Analyzer analyzer = new MetaphoneReplacementAnalyzer();

        IndexWriter writer = new IndexWriter(directory, analyzer, true,
                IndexWriter.MaxFieldLength.UNLIMITED);

        Document doc = new Document();
        doc.add(new Field("contents",
                "cool cat",
                Field.Store.YES,
                Field.Index.ANALYZED));
        writer.addDocument(doc);
        writer.close();

        IndexSearcher searcher = new IndexSearcher(directory);

        Query query = new QueryParser(Version.LUCENE_30,
                "contents", analyzer)
                .parse("kool kat");

        TopDocs hits = searcher.search(query, 1);
        System.out.println("total: " + hits.totalHits);
        int docID = hits.scoreDocs[0].doc;
        doc = searcher.doc(docID);
        System.out.println(doc.get("contents"));   //#D "cool cat"

        searcher.close();
    }

    public static void main(String[] args) throws IOException, ParseException {

        testKoolKat();

    }
}
