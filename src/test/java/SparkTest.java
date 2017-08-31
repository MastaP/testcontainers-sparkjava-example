import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparkTest {

  private static final Logger logger = LoggerFactory.getLogger(SparkTest.class);

  @Rule
  public SparkTestApp app = new SparkTestApp();

  @Test
  public void test() {
    logger.info("Result: " + app.getClient().hello());
  }
}
