package greed.conf;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import greed.conf.schema.GreedConfig;
import greed.util.Configuration;
import greed.util.Debug;
import org.junit.Test;

import java.io.File;

public class ConfigSerializerTest {
    @Test
    public void testSerialize() throws ConfigException {
        ConfigSerializer configSerializer = new ConfigSerializer();
        Config config = ConfigFactory.parseURL(ConfigSerializerTest.class.getResource("/test.conf")).resolve();
        System.out.println(config.toString());
        GreedConfig greedConfig = configSerializer.serializeAndCheck("greed", config.getConfig("greed"), GreedConfig.class);
        System.out.println("done");
    }
}
