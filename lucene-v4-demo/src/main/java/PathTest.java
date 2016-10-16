import java.io.File;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * Created by asnju on 2016/10/15.
 */
public class PathTest {


    public static void main(String[] args) {

        System.out.println(new File("/").getAbsoluteFile());

        System.out.println(new File("./").getAbsoluteFile());


        System.out.println(System.getProperty("user.dir"));

        System.out.println("---------------------\n");

        Properties properties = System.getProperties();
        Set<Object> objects = properties.keySet();

        for (Object obj : objects) {
            System.out.println(obj + " -> " + properties.get(obj));
        }



    }
}
