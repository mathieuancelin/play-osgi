package controllers;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.osgi.framework.Bundle;
import org.osgi.play.OSGi;
import org.osgi.play.OSGiBoostrap;
import org.osgi.play.OSGiUtil;
import play.libs.Files;
import play.mvc.Controller;

public class OSGiAdmin extends Controller {
    
    public static void index() {
        List<Bundle> bundles = Arrays.asList(OSGi.bundleContext().getBundles());
        OSGiUtil osgiUtil = new OSGiUtil();
        render(bundles, osgiUtil);
    }
    
    public static void uploadBundle(File datafile, boolean start) {
        try {
            File newBundle = new File(OSGiBoostrap.uploadedDir, datafile.getName());
            Files.copy(datafile, newBundle);
            Bundle bundle = OSGi.bundleContext().installBundle("file:" + newBundle.getAbsolutePath());
            if (start) {
                bundle.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            String error = "Error while installing uploaded bundle : " + e;
            flash.put("error", error);
        }
        index();
    }
    
    public static void show(long id) {
        Bundle bundle = OSGi.bundleContext().getBundle(id);
        render(bundle);
    }
    
    public static void start(long id) {
        try {
            OSGi.bundleContext().getBundle(id).start();
        } catch (Exception e) {
            e.printStackTrace();
            String error = "Error while starting bundle " + id + " : " + e;
            flash.put("error", error);
        }
        index();
    }
    
    public static void stop(long id) {
        try {
            OSGi.bundleContext().getBundle(id).stop();
        } catch (Exception e) {
            e.printStackTrace();
            String error = "Error while stopping bundle " + id + " : " + e;
            flash.put("error", error);
        }
        index();
    }
    
    public static void uninstall(long id) {
        try {
            OSGi.bundleContext().getBundle(id).uninstall();
        } catch (Exception e) {
            e.printStackTrace();
            String error = "Error while uninstalling bundle " + id + " : " + e;
            flash.put("error", error);
        }
        index();
    }
}
