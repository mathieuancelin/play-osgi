package org.osgi.play;

import org.osgi.framework.Bundle;

public class OSGiUtil {
    
    public static String getState(int i) {
        if (i == Bundle.ACTIVE) {
            return "ACTIVE";
        }
        if (i == Bundle.INSTALLED) {
            return "INSTALLED";
        }
        if (i == Bundle.RESOLVED) {
            return "RESOLVED";
        }
        if (i == Bundle.STARTING) {
            return "STARTING";
        }
        if (i == Bundle.STOPPING) {
            return "STOPPING";
        }
        if (i == Bundle.UNINSTALLED) {
            return "UNINSTALLED";
        }
        return "UNKNOWN";
    }
}
