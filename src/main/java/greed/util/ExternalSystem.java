package greed.util;

import java.io.File;
import java.io.IOException;

/**
 * Greed is good! Cheers!
 */
public class ExternalSystem {

    private static final long DEFAULT_TIMEOUT = 5000L;

    private static class ProcessWrapper implements Runnable {

        private final ProcessBuilder builder;
        private volatile int exitValue;

        public ProcessWrapper(ProcessBuilder builder) {
            this.builder = builder;
            this.exitValue = -1;
        }

        private int fetchExitValue(Process p) {
            try {
                return p.exitValue();
            }
            catch (IllegalThreadStateException e) {
                return -1;
            }
        }

        public int getExitValue() {
            return exitValue;
        }

        @Override
        public void run() {
            Process p;
            try {
                Log.i(String.format("Run command: [%s]", StringUtil.join(builder.command(), " ")));
                p = builder.start();
            } catch (IOException e) {
                exitValue = -1;
                return;
            }

            try {
                exitValue = p.waitFor();
            } catch (InterruptedException e) {
                // Timeout, kill the process
                exitValue = fetchExitValue(p);
                if (exitValue == -1) {
                    Log.i("Process timeout, killing");
                    p.destroy();
                    try {
                        exitValue = p.waitFor();
                    } catch (InterruptedException e1) {
                        exitValue = -1;
                    }
                }
            }
            Log.i("Exit with status: " + exitValue);
        }
    }

    public static int runExternalCommand(File workingDirectory, long timeout, String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command).directory(workingDirectory);
        if (timeout > 0) {
            ProcessWrapper wrapper = new ProcessWrapper(processBuilder);
            Thread wrapperThread = new Thread(wrapper);
            wrapperThread.start();
            try {
                wrapperThread.join(DEFAULT_TIMEOUT);
            } catch (InterruptedException e) {
                Log.e("Main thread should not be interrupted", e);
                return -1;
            }
            wrapperThread.interrupt();
            try {
                wrapperThread.join();
            } catch (InterruptedException e) {
                Log.e("Main thread should not be interrupted", e);
                return -1;
            }
            return wrapper.getExitValue();
        }
        else {
            try {
                processBuilder.start();
                return 0;
            } catch (IOException e) {
                return -1;
            }
        }
    }
}
