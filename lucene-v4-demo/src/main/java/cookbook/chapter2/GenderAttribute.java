package cookbook.chapter2;


import org.apache.lucene.util.Attribute;

/**
 * Created by xwt on 2016/7/18.
 */
public interface GenderAttribute extends Attribute {

    static enum Gender {
        Male, Female, Undefined
    };

    public void setGender(Gender gender);

    public Gender getGender();
}
