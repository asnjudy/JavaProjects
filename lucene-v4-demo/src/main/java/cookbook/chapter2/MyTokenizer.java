package cookbook.chapter2;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.AttributeFactory;

import java.io.Reader;

/**
 * Created by xwt on 2016/7/17.
 */
public class MyTokenizer extends CharTokenizer {

    public MyTokenizer(Reader input) {
        super(input);
    }

    public MyTokenizer(AttributeFactory factory, Reader input) {
        super(factory, input);
    }

    protected boolean isTokenChar(int c) {
        return !Character.isSpaceChar(c);
    }
}
