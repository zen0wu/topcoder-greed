package greed.conf;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;

public class ConfigTest {
    @Test
    public void configValidationTest() {
        Config conf = ConfigFactory.load("default");
        conf = conf.resolve();
        if (conf.hasPath("greed"))
            System.out.println(conf.getConfig("greed").toString());
    }
}
