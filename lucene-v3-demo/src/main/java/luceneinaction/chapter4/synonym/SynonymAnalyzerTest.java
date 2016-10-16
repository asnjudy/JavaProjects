package luceneinaction.chapter4.synonym;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by asnju on 2016/9/15.
 */
public class SynonymAnalyzerTest {

    private SynonymAnalyzer synonymAnalyzer;

    private Directory directory;
    private IndexSearcher indexSearcher;


    public SynonymAnalyzerTest() throws IOException {
        synonymAnalyzer = new SynonymAnalyzer(new SimpleSynonymEngine());
        directory = new RAMDirectory();

    }

    public static void main(String[] args) throws IOException, ParseException {
        SynonymAnalyzerTest test = new SynonymAnalyzerTest();
        test.index();
        test.searchCheck();
        test.searchWithParser();
        test.close();
    }

    public void index() throws IOException {
        IndexWriter indexWriter = new IndexWriter(directory, synonymAnalyzer, IndexWriter.MaxFieldLength.UNLIMITED);
        Document document = new Document();
        document.add(
                new Field(
                        "content",
                        "The quick brown fox jumps over the lazy dog",
                        Field.Store.YES,
                        Field.Index.ANALYZED
                )
        );
        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();

        indexSearcher = new IndexSearcher(directory);
    }

    public void searchCheck() throws IOException {
        TermQuery query = new TermQuery(new Term("content", "hops"));
        /** top 1, 返回最匹配该查询的文档  */
        ScoreDoc scoreDoc = indexSearcher.search(query, 1).scoreDocs[0];
        System.out.println(indexSearcher.doc(scoreDoc.doc));

        PhraseQuery phraseQuery = new PhraseQuery();
        phraseQuery.add(new Term("content", "fox"));
        phraseQuery.add(new Term("content", "hops"));
        ScoreDoc scoreDoc1 = indexSearcher.search(phraseQuery, 1).scoreDocs[0];
        System.out.println(indexSearcher.doc(scoreDoc1.doc));
    }

    public void close() throws IOException {
        indexSearcher.close();
        directory.close();
    }

    public void searchWithParser() throws ParseException, IOException {

        Query query = new QueryParser(
                Version.LUCENE_30,
                "content",
                synonymAnalyzer
        ).parse("\"fox jumps\"");
        System.out.println("With SynonymAnalyzer, \"fox jumps\" parses to " + query.toString("content"));
        ScoreDoc scoreDoc = indexSearcher.search(query, 1).scoreDocs[0];
        System.out.println(indexSearcher.doc(scoreDoc.doc));


        Query query2 = new QueryParser(
                Version.LUCENE_30,
                "content",
                new StandardAnalyzer(Version.LUCENE_30)
        ).parse("\"fox jumps\"");
        System.out.println("With StandardAnalyzer, \"fox jumps\" parses to " + query.toString("content"));
        ScoreDoc scoreDoc2 = indexSearcher.search(query2, 1).scoreDocs[0];
        System.out.println(indexSearcher.doc(scoreDoc2.doc));
    }
}
