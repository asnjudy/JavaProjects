package cookbook.chapter2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

import java.io.Reader;

/**
 * Created by xwt on 2016/7/17.
 */
public class CourtesyTitleAnalyzer extends Analyzer {
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {

        Tokenizer letterTokenizer = new LetterTokenizer(reader);
        TokenStream filter = new CourtesyTitleFilter(letterTokenizer);

        return new TokenStreamComponents(letterTokenizer, filter);
    }
}
