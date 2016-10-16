package cookbook.chapter2;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Created by xwt on 2016/7/18.
 */
public class GenderFilter extends TokenFilter {

    GenderAttribute genderAttribute = addAttribute(GenderAttribute.class);
    CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);

    /**
     * Construct a token stream filtering the given input.
     *
     * @param input
     */
    protected GenderFilter(TokenStream input) {
        super(input);
    }

    public boolean incrementToken() throws IOException {
        if (!input.incrementToken())
            return false;
        genderAttribute.setGender(determineGender(charTermAttribute.toString()));
        return true;
    }


    protected GenderAttribute.Gender determineGender(String term) {
        if (term.equalsIgnoreCase("mr") || term.equalsIgnoreCase("mister")) {
            return GenderAttribute.Gender.Male;
        } else if (term.equalsIgnoreCase("mrs") || term.equalsIgnoreCase("misters")) {
            return GenderAttribute.Gender.Female;
        }
        return GenderAttribute.Gender.Undefined;

    }
}
