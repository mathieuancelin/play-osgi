package org.osgi.play;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import org.osgi.play.injection.BeanSource;
import org.osgi.play.injection.Injector;
import org.osgi.play.injection.OSGiService;
import play.Logger;
import play.PlayPlugin;

public class OSGiPlugin extends PlayPlugin implements BeanSource {

    public static boolean started = true;
    
    private DeployMonitor monitor;

    @Override
    public void onApplicationStart() {
        OSGiBoostrap.createDeployDir();
        started = OSGiBoostrap.initOSGiFramework();
        OSGiBoostrap.installAndStartShell();
        if (started) {
            Injector.inject(this);
        } else {
            throw new IllegalStateException("OSGi container isn't started");
        }
        monitor = new DeployMonitor();
        monitor.start();
    }

    @Override
    public void onApplicationStop() {
        if (started) {
            OSGiBoostrap.stopOSGiFramework();
            monitor.stop();
        }
    }

    @Override
    public <T> T getBeanOfType(Class<T> clazz, Class<?> from, Member m, Annotation... qualifiers) {
        if (started) {
            for (Annotation anno : qualifiers) {
                if (anno.annotationType().equals(OSGiService.class)) {
                    return OSGi.service(clazz);
                }
            }
            Logger.warn("OSGi injection : Ignoring injection point for " + from.getName() + "." + m.getName());
            return null;
        } else {
            throw new IllegalStateException("OSGi container isn't started");
        }
    }
    
    @Override
    public <T> Iterable<T> getBeanCollectionOfType(Class<T> clazz, Class<?> from, Member m, Annotation... qualifiers) {
        if (started) {
            for (Annotation anno : qualifiers) {
                if (anno.annotationType().equals(OSGiService.class)) {
                    if (((OSGiService) anno).value().equals("")) {
                        return OSGi.services(clazz, null);
                    } else {
                        return OSGi.services(clazz, ((OSGiService) anno).value());
                    }
                }
            }
            Logger.warn("OSGi injection : Ignoring injection point for " + from.getName() + "." + m.getName());
            return null;
        } else {
            throw new IllegalStateException("OSGi container isn't started");
        }
    }
}