package cookbook.chapter2;

import org.apache.lucene.util.AttributeImpl;

/**
 * Created by xwt on 2016/7/18.
 */
public class GenderAttributeImpl extends AttributeImpl implements GenderAttribute {

    private Gender gender = Gender.Undefined;

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void clear() {
        gender = Gender.Undefined;
    }

    public void copyTo(AttributeImpl target) {
        ((GenderAttribute) target).setGender(gender);
    }
}
