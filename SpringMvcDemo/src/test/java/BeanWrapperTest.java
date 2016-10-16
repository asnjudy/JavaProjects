import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValue;

/**
 * Created by asnju on 2016/9/16.
 */
public class BeanWrapperTest {

    public static void main(String[] args) {
        User user = new User();
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(user);

        beanWrapper.setPropertyValue("userName", "张三");
        System.out.println(user.getUserName());

        PropertyValue value = new PropertyValue("userName", "李四");
        beanWrapper.setPropertyValue(value);
        System.out.println(user.getUserName());
    }
}
