package org.osgi.play;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.felix.framework.FrameworkFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

public class OSGiBoostrap {
    
    public static final File deployDir = new File("autodeploy");
    
    public static final File dataDir = new File("/tmp/osgidata");
    
    public static final File uploadedDir = new File(dataDir, "uploaded");
    
    public static void createDeployDir() {
        if (!deployDir.exists()) {
            deployDir.mkdirs();
        }
        cascadeDelete(dataDir);
    }
    
    private static void cascadeDelete(File in) {
        if (in != null && in.listFiles() != null) {
            for (File f : in.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else {
                    cascadeDelete(f);
                    f.delete();
                }
            }
        }
    }

    public static boolean initOSGiFramework() {
        try {
            FrameworkFactory factory = new FrameworkFactory();
            Map<String, String> config = new HashMap<String, String>();
            config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "models, play");
            config.put(Constants.FRAMEWORK_STORAGE, dataDir.getAbsolutePath());
            config.put(Constants.FRAMEWORK_STORAGE_CLEAN, "true");
            config.put(Constants.FRAMEWORK_EXECUTIONENVIRONMENT, "J2SE-1.6");
            config.put("osgi.shell.telnet", "on");
            config.put("osgi.shell.telnet.port", "6666");
            OSGi.osgiFramework = factory.newFramework(config);
            OSGi.osgiFramework.start();
            if (!uploadedDir.exists()) {
                uploadedDir.mkdirs();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void installAndStartShell() {
        try {
            BundleContext context = OSGi.osgiFramework.getBundleContext();
            List<Bundle> installedBundles = new LinkedList<Bundle>();
            installedBundles.add(context.installBundle(
                    "file:lib/org.apache.felix.shell-1.0.0.jar"));
            installedBundles.add(context.installBundle(
                    "file:lib/org.apache.felix.shell.remote-1.0.2.jar"));
            for (Bundle bundle : installedBundles) {
                if (bundle.getHeaders().get(Constants.BUNDLE_ACTIVATOR) != null) {
                    if (bundle.getHeaders().get(Constants.FRAGMENT_HOST) == null) {
                        bundle.start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopOSGiFramework() {
        try {
            OSGi.osgiFramework.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
