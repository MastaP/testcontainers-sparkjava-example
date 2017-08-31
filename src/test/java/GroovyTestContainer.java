import com.github.dockerjava.api.command.InspectContainerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GroovyTestContainer<SELF extends GroovyTestContainer<SELF>> extends GenericContainer<SELF> {

    private static final Logger logger = LoggerFactory.getLogger(GroovyTestContainer.class);
    protected List<String> javaOpts = new ArrayList<>();

    public GroovyTestContainer() {
        super("groovy:2.5.0-beta-1-jdk8");
        javaOpts.add("-Dgrape.report.downloads=true");
        javaOpts.add("-Divy.message.logger.level=2");
        javaOpts.add("-Djava.security.egd=file:/dev/./urandom");
        withStartupTimeout(Duration.ofMinutes(3));

        try {
            // Cache Grapes
            String canonicalPath = new File(System.getProperty("user.home") + "/.groovy/grapes").getCanonicalPath();
            logger.info("Adding system binding: " + canonicalPath);
            addFileSystemBind(canonicalPath, "/home/groovy/.groovy/grapes", BindMode.READ_WRITE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Starting groovy container");
    }

    @Override
    protected void configure() {
        super.configure();
        addEnv("JAVA_OPTS", String.join(" ", javaOpts));
    }

    public String getURL(int port) {
        return "http://" + getContainerIpAddress() + ":" + getMappedPort(port);
    }

    @Override
    protected void containerIsStarting(InspectContainerResponse containerInfo) {
        super.containerIsStarting(containerInfo);
        followOutput(new Slf4jLogConsumer(logger));
    }
}
