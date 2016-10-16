package cookbook.chapter2;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xwt on 2016/7/17.
 */
public class CourtesyTitleFilter extends TokenFilter {


    Map<String, String> courtesyTitleMap = new HashMap<String, String>();
    private CharTermAttribute charTermAttribute;

    /**
     * Construct a token stream filtering the given input.
     *
     * @param input
     */
    protected CourtesyTitleFilter(TokenStream input) {
        super(input);
        charTermAttribute = addAttribute(CharTermAttribute.class);
        courtesyTitleMap.put("Dr", "doctor");
        courtesyTitleMap.put("Mr", "mister");
        courtesyTitleMap.put("Mrs", "miss");
    }

    public boolean incrementToken() throws IOException {

        if (!input.incrementToken()) return false;

        String small = charTermAttribute.toString();
        if (courtesyTitleMap.containsKey(small))
            charTermAttribute.setEmpty().append(courtesyTitleMap.get(small));

        return true;
    }
}
