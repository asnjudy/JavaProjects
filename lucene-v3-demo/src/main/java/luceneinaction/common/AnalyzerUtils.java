package luceneinaction.common;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by asnju on 2016/9/15.
 */
public class AnalyzerUtils {

    /**
     * 只输出语汇单元对应的单词
     *
     * @param analyzer
     * @param text
     * @throws IOException
     */
    public static void displayTokens(Analyzer analyzer, String text) throws IOException {

        // 取得tokenStream
        TokenStream tokenStream = analyzer.tokenStream("contents", new StringReader(text));

        TermAttribute termAttribute = tokenStream.addAttribute(TermAttribute.class);
        while (tokenStream.incrementToken()) {
            System.out.print("[" + termAttribute.term() + "]");
        }
        System.out.println();
    }

    /**
     * 输出
     * 1）该语汇单元位置（相邻两个语汇单元间的位置增量为1）
     * 2）语汇单元中的单词
     * 3）语汇单元中的单词 在原始文本中的起始和终止偏移量
     * 4）语汇单元类型
     *
     * @param analyzer
     * @param text
     * @throws IOException
     */
    public static void displayTokensWithFullDetails(Analyzer analyzer, String text) throws IOException {

        TokenStream tokenStream = analyzer.tokenStream("contents", new StringReader(text));

        TermAttribute termAttr = tokenStream.addAttribute(TermAttribute.class);
        PositionIncrementAttribute positionIncrAttr = tokenStream.addAttribute(PositionIncrementAttribute.class);
        OffsetAttribute offsetAttr = tokenStream.addAttribute(OffsetAttribute.class);
        TypeAttribute typeAttr = tokenStream.addAttribute(TypeAttribute.class);

        int position = 0;
        while (tokenStream.incrementToken()) {
            int increment = positionIncrAttr.getPositionIncrement();

            if (increment > 0) {
                position = position + increment;
                System.out.print(position + ":");
            }
            System.out.println("["
                    + termAttr.term() + ":"
                    + offsetAttr.startOffset() + "->"
                    + offsetAttr.endOffset() + ":"
                    + typeAttr.type() + "]");
        }
        final AttributeSource.State state = tokenStream.captureState();
        tokenStream.restoreState(null);
    }

}
