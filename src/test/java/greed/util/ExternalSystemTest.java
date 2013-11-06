package greed.util;

import greed.conf.schema.LoggingConfig;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Greed is good! Cheers!
 */
public class ExternalSystemTest {

    @Before
    public void init() {
        LoggingConfig config = new LoggingConfig();
        config.setLogLevel("DEBUG");
        config.setLogToStderr(true);
        Log.initialize(config);
    }

    @Test
    public void testJava() {
        // Returns 1
        Assert.assertEquals(ExternalSystem.runExternalCommand("java"), 1);
    }

    @Test
    public void testLs() {
        Assume.assumeTrue(!TestUtil.isWindows());
        Assert.assertEquals(ExternalSystem.runExternalCommand("ls"), 0);
    }

    @Test
    public void testCat() {
        // cat with no arguments will hang
        Assume.assumeTrue(!TestUtil.isWindows());
        Assert.assertEquals(ExternalSystem.runExternalCommand("cat"), 143);
    }

    @Test
    public void testCp() {
        Assume.assumeTrue(!TestUtil.isWindows());
        Assert.assertEquals(ExternalSystem.runExternalCommand("touch", "a"), 0);
        Assert.assertTrue(new File("a").exists());
        Assert.assertEquals(ExternalSystem.runExternalCommand("cp", "a", "b"), 0);
        Assert.assertTrue(new File("b").exists());
        Assert.assertTrue(new File("a").delete());
        Assert.assertTrue(new File("b").delete());
    }
}
