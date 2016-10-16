package luceneinaction.chapter4.codec;

import luceneinaction.common.AnalyzerUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.TokenStream;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by asnju on 2016/9/15.
 */
public class MetaphoneReplacementAnalyzer extends Analyzer {


    public static void main(String[] args) throws IOException {

        MetaphoneReplacementAnalyzer analyzer = new MetaphoneReplacementAnalyzer();

        AnalyzerUtils.displayTokens(analyzer, "The quick brown fox jumped over the lazy dog");
        AnalyzerUtils.displayTokens(analyzer, "The quick brown phox jumped ovvar the lazi dog");
        /**
         * [0][KK][BRN][FKS][JMPT][OFR][0][LS][TK]
         [0][KK][BRN][FKS][JMPT][OFR][0][LS][TK]
         */
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new MetaphoneReplacementFilter(
                new LetterTokenizer(reader)
        );
    }
}
