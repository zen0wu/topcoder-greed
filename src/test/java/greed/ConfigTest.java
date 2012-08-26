package greed;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigTest {
    public static void main(String[] args) {
        Config conf = ConfigFactory.load("default");
        System.out.println(conf.getConfig("greed").toString());
    }
}
