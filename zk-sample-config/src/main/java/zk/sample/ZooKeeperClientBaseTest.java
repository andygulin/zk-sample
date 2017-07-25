package zk.sample;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

public abstract class ZooKeeperClientBaseTest {

    protected static String SERVERS = "";

    static {
        Resource resource = new ClassPathResource("zookeeper.properties");
        try {
            Configuration configuration = new PropertiesConfiguration(resource.getFile());
            SERVERS = configuration.getString("zk.servers");
        } catch (ConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }
}
