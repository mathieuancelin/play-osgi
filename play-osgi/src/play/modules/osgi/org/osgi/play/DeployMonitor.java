package org.osgi.play;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import play.Logger;

public class DeployMonitor implements Runnable {
    
    public static Map<File, Bundle> deployed = new HashMap<File, Bundle>();
    
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    
    public void start() {
        executor.scheduleAtFixedRate(this, 5, 5, TimeUnit.SECONDS);
    }
    
    public void stop() {
        executor.shutdown();
        executor.shutdownNow();
    }

    @Override
    public void run() {
        if (OSGiBoostrap.deployDir.exists()) {
            for (File f : OSGiBoostrap.deployDir.listFiles()) {
                if (f.getName().endsWith(".jar")) {
                    if (!deployed.containsKey(f)) {
                        try {
                            Logger.info("Installing bundle : " + f.getName());
                            Bundle b = OSGi.bundleContext().installBundle("file:" + f.getAbsolutePath());
                            deployed.put(f, b);
                            if (b.getHeaders().get(Constants.BUNDLE_ACTIVATOR) != null) {
                                if (b.getHeaders().get(Constants.FRAGMENT_HOST) == null) {
                                    b.start();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            for (File f : deployed.keySet()) {
                if (!f.exists()) {
                    try {
                        Logger.info("Uninstalling bundle : " + f.getName());
                        Bundle b = deployed.get(f);
                        b.stop();
                        b.uninstall();
                        deployed.remove(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
