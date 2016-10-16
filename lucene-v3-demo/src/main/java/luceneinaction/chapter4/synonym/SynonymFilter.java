package luceneinaction.chapter4.synonym;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.util.Stack;

/**
 * Created by asnju on 2016/9/15.
 */
public class SynonymFilter extends TokenFilter {

    public static final String TOKEN_TYPE_SYNONYM = "synonym";
    private final TermAttribute termAttr;
    private final PositionIncrementAttribute posIncrAttr;
    private Stack<String> synonymStack;
    private SynonymEngine engine;
    private AttributeSource.State current;

    public SynonymFilter(TokenStream in, SynonymEngine engine) {
        super(in);
        synonymStack = new Stack<String>();
        this.engine = engine;

        this.termAttr = addAttribute(TermAttribute.class);
        this.posIncrAttr = addAttribute(PositionIncrementAttribute.class);
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (synonymStack.size() > 0) {
            String syn = synonymStack.pop();
            restoreState(current);
            termAttr.setTermBuffer(syn);
            posIncrAttr.setPositionIncrement(0);
            return true;
        }
        if (!input.incrementToken())
            return false;

        if (addAliasesToStack())
            current = captureState();

        return true;
    }

    private boolean addAliasesToStack() throws IOException {
        String[] synonyms = engine.getSynonyms(termAttr.term());

        if (synonyms == null)
            return false;

        for (String synonym : synonyms) {
            synonymStack.push(synonym);
        }
        return true;
    }
}
