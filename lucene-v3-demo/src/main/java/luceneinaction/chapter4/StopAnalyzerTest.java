package luceneinaction.chapter4;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

class StopAnalyzerFlawed extends Analyzer {

    private Set stopWords;

    public StopAnalyzerFlawed() {
        stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
    }

    public StopAnalyzerFlawed(String[] stopWords) {
        this.stopWords = StopFilter.makeStopSet(stopWords);
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {

        return new LowerCaseFilter(
                new StopFilter(true,
                        new LetterTokenizer(reader),
                        stopWords));
    }
}

/**
 * Created by asnju on 2016/9/15.
 */
class StopAnalyzer2 extends Analyzer {

    private Set stopWords;

    public StopAnalyzer2() {
        stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
    }

    public StopAnalyzer2(String[] stopWords) {
        this.stopWords = StopFilter.makeStopSet(stopWords);
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new StopFilter(
                true,
                new LowerCaseFilter(new LetterTokenizer(reader)),
                stopWords);
    }
}

public class StopAnalyzerTest {

    public static void main(String[] args) throws IOException {
        Analyzer analyzer = new StopAnalyzerFlawed();
        String input = "The quick brown A a  ...";

        TokenStream tokenStream = analyzer.tokenStream("field", new StringReader(input));
        TermAttribute termAttr = tokenStream.addAttribute(TermAttribute.class);

        while (tokenStream.incrementToken()) {
            System.out.println(termAttr.term());
        }

        tokenStream.close();
    }
}
