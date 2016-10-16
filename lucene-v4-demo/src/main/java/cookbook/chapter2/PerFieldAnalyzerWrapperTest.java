package cookbook.chapter2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xwt on 2016/7/17.
 */
public class PerFieldAnalyzerWrapperTest {

    public static void main(String[] args) {

        Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
        analyzerPerField.put("myfield", new WhitespaceAnalyzer());

        PerFieldAnalyzerWrapper defaultAnalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);

        TokenStream tokenStream = null;
        OffsetAttribute offsetAttribute = null;
        CharTermAttribute charTermAttribute = null;

        try {
            tokenStream = defaultAnalyzer.tokenStream("myfield", new StringReader("lucene.apache.org AB-978"));
            offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
            charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();

            System.out.println("== Processing field 'myfield' using WhitespaceAnalyzer (per field) ==");

            while (tokenStream.incrementToken()) {
                System.out.println(charTermAttribute.toString());
                System.out.println("token start offset: " + offsetAttribute.startOffset());
                System.out.println("token end offset: " + offsetAttribute.endOffset());
            }
            tokenStream.end();

            tokenStream = defaultAnalyzer.tokenStream("content", new StringReader("lucene.apache.org AB-978"));
            offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
            charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();

            System.out.println("== Processing field 'content' using StandardAnalyzer (per field) ==");

            while (tokenStream.incrementToken()) {
                System.out.println(charTermAttribute.toString());
                System.out.println("token start offset: " + offsetAttribute.startOffset());
                System.out.println("token end offset: " + offsetAttribute.endOffset());
            }
            tokenStream.end();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                tokenStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            defaultAnalyzer.close();
        }

    }
}
