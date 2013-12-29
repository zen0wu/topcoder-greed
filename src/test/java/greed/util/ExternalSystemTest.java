package greed.util;

import greed.conf.schema.LoggingConfig;

import java.io.File;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Greed is good! Cheers!
 */
public class ExternalSystemTest {

    @Before
    public void init() {
        LoggingConfig config = new LoggingConfig();
        config.setLogLevel(LoggingConfig.LoggingLevel.DEBUG);
        config.setLogToStderr(true);
        config.setLogFolder("Logs");
        Log.initialize(config);
    }

    @Test
    public void testJava() {
        // Returns 1
        Assert.assertEquals(ExternalSystem.runExternalCommand(new File("."), 1000, "java"), 1);
    }

    @Test
    public void testLs() {
        Assume.assumeTrue(!TestUtil.isWindows());
        Assert.assertEquals(ExternalSystem.runExternalCommand(new File("."), 1000, "ls"), 0);
    }

    @Test
    public void testCat() {
        // cat with no arguments will hang
        Assume.assumeTrue(!TestUtil.isWindows());
        // the exit code 143 corresponds to SIGTERM in POSIX, which is sent when we kill the process
        Assert.assertEquals(ExternalSystem.runExternalCommand(new File("."), 5000, "cat"), 143);
    }

    @Test
    public void testCp() {
        Assume.assumeTrue(!TestUtil.isWindows());
        Assert.assertEquals(ExternalSystem.runExternalCommand(new File("."), 5000, "touch", "a"), 0);
        Assert.assertTrue(new File("a").exists());
        Assert.assertEquals(ExternalSystem.runExternalCommand(new File("."), 5000, "cp", "a", "b"), 0);
        Assert.assertTrue(new File("b").exists());
        Assert.assertTrue(new File("a").delete());
        Assert.assertTrue(new File("b").delete());
    }
}
