package luceneinaction.chapter4.synonym;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * Created by asnju on 2016/9/15.
 */
public class SynonymAnalyzer extends Analyzer {

    private SynonymEngine engine;

    public SynonymAnalyzer(SynonymEngine engine) {
        this.engine = engine;
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {

        TokenStream tokenStream = new SynonymFilter(
                new StopFilter(true,
                        new LowerCaseFilter(
                                new StandardFilter(
                                        new StandardTokenizer(Version.LUCENE_30, reader)
                                )
                        ),
                        StopAnalyzer.ENGLISH_STOP_WORDS_SET
                ),
                engine
        );

        return tokenStream;
    }
}
