package luceneinaction.chapter4.codec;

import org.apache.commons.codec.language.Metaphone;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;

/**
 * Created by asnju on 2016/9/15.
 */
public class MetaphoneReplacementFilter extends TokenFilter {

    private static final String METAPHONE = "metaphone";

    private Metaphone metaphone = new Metaphone();
    private TermAttribute termAttr;
    private TypeAttribute typeAttr;


    /**
     * Construct a token stream filtering the given input.
     *
     * @param input
     */
    protected MetaphoneReplacementFilter(TokenStream input) {
        super(input);
        termAttr = addAttribute(TermAttribute.class);
        typeAttr = addAttribute(TypeAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!input.incrementToken())
            return false;

        String encoded;

        encoded = metaphone.encode(termAttr.term());
        termAttr.setTermBuffer(encoded);
        typeAttr.setType(METAPHONE);
        return true;
    }
}
