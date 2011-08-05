package controllers;

import javax.inject.Inject;
import org.osgi.play.OSGi;
import org.osgi.play.injection.OSGiService;
import play.mvc.*;

import org.osgi.service.packageadmin.PackageAdmin;

public class Application extends Controller {
    
    @Inject @OSGiService
    static Iterable<PackageAdmin> admins;

    public static void index() {
        //PackageAdmin admin = OSGi.service(PackageAdmin.class);
        try {
            for (PackageAdmin admin : admins) {
                System.out.println(admin.getExportedPackage("org.osgi.service.packageadmin"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        render();
    }
}