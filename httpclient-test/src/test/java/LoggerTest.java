import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by asnju on 2017/1/13.
 */
public class LoggerTest {

    private static Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    public static void main(String[] args) {
        logger.info("test log message");
    }
}
