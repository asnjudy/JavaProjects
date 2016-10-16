package cookbook.chapter2;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by xwt on 2016/7/17.
 */
public class LuceneTest {

    public static void main(String[] args) {

        StringReader reader = new StringReader("Lucene is mainly used for information retrieval and you can read more about it at lucene.apache.org.");
        StandardAnalyzer stdAnalyzer = new StandardAnalyzer();
        TokenStream tokenStream = null;

        try {
            tokenStream = stdAnalyzer.tokenStream("field", reader);

            OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
            CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();

            while (tokenStream.incrementToken()) {
                String token = termAttribute.toString();
                System.out.println("[" + token + "]");
                System.out.println("Token starting offset: " + offsetAttribute.startOffset());
                System.out.println("Token ending offset: " + offsetAttribute.endOffset());
                System.out.println("");
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
            stdAnalyzer.close();
        }

    }
}
