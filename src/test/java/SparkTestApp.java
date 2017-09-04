import com.github.dockerjava.api.command.InspectContainerResponse;
import feign.Feign;
import feign.Headers;
import feign.RequestLine;
import feign.httpclient.ApacheHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.wait.WaitStrategy;

public class SparkTestApp extends GroovyTestContainer<SparkTestApp> {
    private static final Logger logger = LoggerFactory.getLogger(SparkTestApp.class);

    public static final int PORT = 4567;
    public static String defaultAppName = "spark.groovy";
    private Client client;

    public SparkTestApp() {
        this(defaultAppName);
    }

    public SparkTestApp(String fileName) {
        withExposedPorts(PORT);
        withClasspathResourceMapping(fileName, "/app/test.groovy", BindMode.READ_ONLY);
        withCommand("/opt/groovy/bin/groovy /app/test.groovy");
    }

    public SparkTestApp withWaitStrategy(WaitStrategy ws) {
        setWaitStrategy(ws);
        return this;
    }

    @Override
    protected void containerIsStarting(InspectContainerResponse containerInfo) {
        super.containerIsStarting(containerInfo);
        client = buildClient();
        logger.info("started spark container at " + getURL());
    }

    private Client buildClient() {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .target(Client.class, getURL());
    }

    public Client getClient() {
        if (client == null) {
            client = buildClient();
        }
        return client;
    }

    public String getURL() {
        return getURL(PORT);
    }

    public interface Client {

        @Headers("Content-Type: text/xml")
        @RequestLine("GET /hello")
        String hello();
    }
}
