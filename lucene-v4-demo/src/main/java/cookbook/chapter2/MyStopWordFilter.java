package cookbook.chapter2;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;

/**
 * Created by xwt on 2016/7/17.
 */
public class MyStopWordFilter extends TokenFilter {

    private CharTermAttribute charTermAttribute;
    private PositionIncrementAttribute posIncrAttribute;

    /**
     * Construct a token stream filtering the given input.
     *
     * @param input
     */
    protected MyStopWordFilter(TokenStream input) {
        super(input);
        charTermAttribute = addAttribute(CharTermAttribute.class);
        posIncrAttribute = addAttribute(PositionIncrementAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {

        int extraIncrement = 0;
        boolean returnValue = false;

        while (input.incrementToken()) {
            if (StopAnalyzer.ENGLISH_STOP_WORDS_SET.contains(charTermAttribute.toString())) {
                extraIncrement++;
                continue;
            }
            returnValue = true;
            break;
        }

        if (extraIncrement > 0) {
            posIncrAttribute.setPositionIncrement(posIncrAttribute.getPositionIncrement() + extraIncrement);
        }

        return returnValue;
    }
}
