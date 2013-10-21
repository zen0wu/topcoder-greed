package greed;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;

import java.io.File;

public class ConfigTest {
    @Test
    public void configValidationTest() {
        Config conf = ConfigFactory.load("default");
        if (conf.hasPath("greed"))
            System.out.println(conf.getConfig("greed").toString());
    }
}
