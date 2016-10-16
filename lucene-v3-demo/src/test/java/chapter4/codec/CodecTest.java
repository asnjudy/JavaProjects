package chapter4.codec;

import junit.framework.TestCase;
import org.apache.commons.codec.language.Metaphone;

/**
 * Created by asnju on 2016/9/15.
 */
public class CodecTest extends TestCase {

    public void testMetaphone() {

        Metaphone metaphone = new Metaphone();

        assertEquals(metaphone.encode("cute"), metaphone.encode("cat"));

        System.out.println(metaphone.encode("cute"));
        System.out.println(metaphone.encode("cat"));
    }
}
