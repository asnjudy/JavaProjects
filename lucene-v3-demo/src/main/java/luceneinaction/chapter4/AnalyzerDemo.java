package luceneinaction.chapter4;

import luceneinaction.common.AnalyzerUtils;

import java.io.IOException;

/**
 * Created by asnju on 2016/9/15.
 */
public class AnalyzerDemo {

    private static final String[] examples = {
            "The quick brown fox jumped over the lazy dog",
            "XY&Z Corporation - xyz@example.com"
    };


    public static void analyze(String text) throws IOException {

        System.out.println("Analyzing \"" + text + "\"");
        SimpleAnalyzer simpleAnalyzer = new SimpleAnalyzer();

        AnalyzerUtils.displayTokensWithFullDetails(simpleAnalyzer, text);
    }

    public static void main(String[] args) throws IOException {
        for (String text : examples) {
            analyze(text);
        }
    }
}
