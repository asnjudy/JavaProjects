package luceneinaction.chapter4;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by asnju on 2016/9/15.
 */
public class SimpleAnalyzer extends Analyzer {

    @Override
    public TokenStream tokenStream(String s, Reader reader) {
        return new LowerCaseTokenizer(reader);
    }


    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
        Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream();

        if (tokenizer == null) {
            tokenizer = new LowerCaseTokenizer(reader);
            setPreviousTokenStream(tokenizer);
        } else {
            tokenizer.reset(reader);
        }
        return tokenizer;
    }
}
