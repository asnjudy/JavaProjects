package luceneinaction.chapter5.sort;

import luceneinaction.common.SearchUtils;
import luceneinaction.common.TestUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 * Created by asnju on 2016/9/15.
 */
public class SortingExample {



    public static void main(String[] args) throws ParseException, IOException {
        Query allBooks = new MatchAllDocsQuery();

        /**
         * 在contents域上构造查询
         */
        QueryParser queryParser = new QueryParser(Version.LUCENE_30, "contents", new StandardAnalyzer(Version.LUCENE_30));

        BooleanQuery query = new BooleanQuery();
        query.add(allBooks, BooleanClause.Occur.SHOULD);
        query.add(queryParser.parse("java OR action"), BooleanClause.Occur.SHOULD);



        System.out.println("******* 按相关度评分排序 ****************");
        SearchUtils.displayResults(query, Sort.RELEVANCE);  //按相关度评分排序

        //
        System.out.println("******* MatchAllDocsQuery **************");
        SearchUtils.displayResults(allBooks, Sort.RELEVANCE);

        System.out.println("******* 按文档索引顺序排序 ****************");
        SearchUtils.displayResults(query, Sort.INDEXORDER); //按文档索引顺序排序

        System.out.println("******* 按 category 域 进行排序 ****************");
        SearchUtils.displayResults(query, new Sort(new SortField("category", SortField.STRING)));

        System.out.println("******* 按 pubmonth 域 进行排序 ****************");
        SearchUtils.displayResults(query, new Sort(new SortField("pubmonth", SortField.INT, true)));

        System.out.println("******* category, filed_score, pubmonth ****************");
        SearchUtils.displayResults(query,
                new Sort(
                        new SortField("category", SortField.STRING),  //第一排序条件，按文档分类排序
                        SortField.FIELD_SCORE,
                        new SortField("pubmonth", SortField.INT, true)
                ));

        System.out.println("******* filed_score, category ****************");
        SearchUtils.displayResults(query,
                new Sort(new SortField[]{
                        SortField.FIELD_SCORE,
                        new SortField("category", SortField.STRING)}
                )
        );
    }
}
