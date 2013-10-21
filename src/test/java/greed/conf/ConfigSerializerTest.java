package greed.conf;

import greed.conf.schema.GreedConfig;

import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigSerializerTest {
    @Test
    public void testSerialize() throws ConfigException {
        ConfigSerializer configSerializer = new ConfigSerializer();
        Config config = ConfigFactory.parseURL(ConfigSerializerTest.class.getResource("/default.conf")).resolve();
        System.out.println(config.toString());
        GreedConfig greedConfig = configSerializer.serializeAndCheck("greed", config.getConfig("greed"), GreedConfig.class);
        System.out.println("done");
    }
}
