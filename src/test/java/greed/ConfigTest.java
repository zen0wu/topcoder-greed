package greed;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class ConfigTest {
    public static void main(String[] args) {
        Config conf = ConfigFactory.load("default");
        if (conf.hasPath("greed"))
            System.out.println(conf.getConfig("greed").toString());
        conf = ConfigFactory.parseFile(new File(System.getProperty("user.dir") + "/src/main/resources/default.conf")).resolve();
        if (conf.hasPath("greed"))
            System.out.println(conf.getConfig("greed").toString());
    }
}
