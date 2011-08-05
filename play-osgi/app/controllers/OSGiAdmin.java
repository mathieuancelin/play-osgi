package controllers;

import java.util.Arrays;
import java.util.List;
import org.osgi.framework.Bundle;
import org.osgi.play.OSGi;
import org.osgi.play.OSGiUtil;
import play.mvc.Controller;

public class OSGiAdmin extends Controller {
    
    public static void index() {
        List<Bundle> bundles = Arrays.asList(OSGi.bundleContext().getBundles());
        OSGiUtil osgiUtil = new OSGiUtil();
        render(bundles, osgiUtil);
    }
    
    public static void start(long id) {
        try {
            OSGi.bundleContext().getBundle(id).start();
        } catch (Exception e) {
            e.printStackTrace();
            String error = "Error while starting bundle " + id;
            flash.put("error", error);
        }
        index();
    }
    
    public static void stop(long id) {
        try {
            OSGi.bundleContext().getBundle(id).stop();
        } catch (Exception e) {
            e.printStackTrace();
            String error = "Error while stopping bundle " + id;
            flash.put("error", error);
        }
        index();
    }
    
    public static void uninstall(long id) {
        try {
            OSGi.bundleContext().getBundle(id).uninstall();
        } catch (Exception e) {
            e.printStackTrace();
            String error = "Error while uninstalling bundle " + id;
            flash.put("error", error);
        }
        index();
    }
}
