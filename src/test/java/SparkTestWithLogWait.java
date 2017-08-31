import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.wait.LogMessageWaitStrategy;

public class SparkTestWithLogWait {

  private static final Logger logger = LoggerFactory.getLogger(SparkTest.class);

  @Rule
  public SparkTestApp app = new SparkTestApp().withWaitStrategy(new LogMessageWaitStrategy().withRegEx("(?s).*spark app started on port.*"));

  @Test
  public void test() {
    logger.info("Result: " + app.getClient().hello());
  }
}
