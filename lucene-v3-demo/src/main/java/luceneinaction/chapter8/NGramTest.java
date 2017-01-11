package luceneinaction.chapter8;

import luceneinaction.common.AnalyzerUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by asnju on 2017/1/11.
 */
public class NGramTest {


    /***
     * Ngram切割法
     */
    private static class NGramAnalyzer extends Analyzer {
        @Override
        public TokenStream tokenStream(String fieldName, Reader reader) {
            return new NGramTokenFilter(new KeywordTokenizer(reader), 2, 4);
        }
    }

    /**
     * 从首字母开始切
     */
    private static class FrontEdgeNGramAnalyzer extends Analyzer {
        @Override
        public TokenStream tokenStream(String fieldName, Reader reader) {
            return new EdgeNGramTokenFilter(new KeywordTokenizer(reader), EdgeNGramTokenFilter.Side.FRONT, 1, 4);
        }
    }

    /**
     * 从串尾开始切
     */
    private static class BackEdgeNGramAnalyzer extends Analyzer {

        @Override
        public TokenStream tokenStream(String fieldName, Reader reader) {
            return new EdgeNGramTokenFilter(new KeywordTokenizer(reader), EdgeNGramTokenFilter.Side.BACK, 1, 4);
        }
    }

    public static void main(String[] args) throws IOException {
        AnalyzerUtils.displayTokensWithFullDetails(new BackEdgeNGramAnalyzer(), "lettuce");
    }
}
