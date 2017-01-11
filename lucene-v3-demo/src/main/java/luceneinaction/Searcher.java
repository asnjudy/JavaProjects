package luceneinaction;

import luceneinaction.common.SearchUtils;
import luceneinaction.common.TestUtil;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * Created by xwt on 2016/7/10.
 */
public class Searcher {

    public static void search(String indexDir, String q) throws IOException, ParseException {

        Directory dir = FSDirectory.open(new File(indexDir)); //打开索引文件用于搜索
        IndexSearcher searcher = new IndexSearcher(dir); //以索引文件目录为参数构造“IndexSearcher- 索引搜索器”实例


        QueryParser parser = new QueryParser(Version.LUCENE_30, "title", new StandardAnalyzer(Version.LUCENE_30));
        Query query = parser.parse(q); //解析查询字符串

        long start = System.currentTimeMillis();

        /**
         * Topic对象只包括对应文档的引用
         */
        TopDocs hits = searcher.search(query, 10); //搜索索引，以Topic对象返回搜索结果集
        long end = System.currentTimeMillis();

        //记录搜索状态
        System.err.println("Found " + hits.totalHits
                + " document(s) (in " + (end - start)
                + " milliseconds) that matched query '"
                + q
                + "':"
        );

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc); //返回匹配文本
            System.out.println(doc.get("fullpath"));
        }
        searcher.close();
    }






    public static void test() throws ParseException, IOException {
        Query allBooks = new MatchAllDocsQuery();
        QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new StandardAnalyzer(Version.LUCENE_30));
        BooleanQuery query = new BooleanQuery();

        query.add(allBooks, BooleanClause.Occur.SHOULD);
        query.add(parser.parse("java OR action"), BooleanClause.Occur.SHOULD);

        Directory directory = TestUtil.getBookIndexDirectory(); //打开索引文件用于搜索
        SearchUtils.displayResults(query, Sort.RELEVANCE);

        System.out.println("**********************************************");
        SearchUtils.displayResults(query, Sort.INDEXORDER);

    }

    public static void main(String[] args) throws IOException, ParseException {
       // String indexDir = "index\\books";
        //String q = "junit";
       // search(indexDir, q);

        test();
    }
}
