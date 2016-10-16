package luceneinaction.chapter4.synonym;

import java.io.IOException;

/**
 * Created by asnju on 2016/9/15.
 */
public interface SynonymEngine {

    String[] getSynonyms(String s) throws IOException;
}
