package org.osgi.play.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.inject.Inject;
import play.Play;
import play.classloading.enhancers.ControllersEnhancer.ControllerSupport;
import play.jobs.Job;
import play.mvc.Mailer;

public class Injector {
    
    /**
     * For now, inject beans in controllers
     */
    public static void inject(BeanSource source) {
        List<Class> classes = Play.classloader.getAssignableClasses(ControllerSupport.class);
        classes.addAll(Play.classloader.getAssignableClasses(Mailer.class));
        classes.addAll(Play.classloader.getAssignableClasses(Job.class));
        for(Class<?> clazz : classes) {
            for(Method method : clazz.getDeclaredMethods()) {
                if(Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(Inject.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    // TODO : use TypeLiteral when generic params
                    Type[] genericParameterTypes = method.getGenericParameterTypes();
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                    Object[] parameters = new Object[parameterTypes.length];
                    for (int j = 0; j < parameterTypes.length; j++) {
                        Object value = null;
                        if (parameterTypes[j].equals(Iterable.class)) {
                            value = source.getBeanCollectionOfType(getGenericType(genericParameterTypes[j]),
                                    clazz, method, parameterAnnotations[j]);
                        } else {
                            value = source.getBeanOfType(parameterTypes[j],
                                    clazz, method, parameterAnnotations[j]);
                        }
                        parameters[j] = value;
                    }
                    boolean accessible = method.isAccessible();
                    // set a private method as public method to invoke it
                    if (!accessible) {
                        method.setAccessible(true);
                    }
                    // invocation of the method with rights parameters
                    try {
                        method.invoke(null, parameters);
                    } catch(RuntimeException e) {
                        throw e;
                    } catch(Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        // if method was private, then put it private back
                        if (!accessible) {
                            method.setAccessible(accessible);
                        }
                    }
                }
            }
            for(Field field : clazz.getDeclaredFields()) {
                if(Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Inject.class)) {
                    Class<?> type = field.getType();
                    Type genericType = field.getGenericType();
                    if (type.equals(Iterable.class)) {
                        Object value = null;
                        field.setAccessible(true);
                        try {
                            value = source.getBeanCollectionOfType(getGenericType(genericType), clazz, field, field.getAnnotations());
                            field.set(null, value);
                        } catch(RuntimeException e) {
                            throw e;
                        } catch(Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Object value = null;
                        field.setAccessible(true);
                        try {
                            value = source.getBeanOfType(type, clazz, field, field.getAnnotations());
                            field.set(null, value);
                        } catch(RuntimeException e) {
                            throw e;
                        } catch(Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } 
    }
    
    private static Class<?> getGenericType(Type t) {
        return (Class<?>) ((ParameterizedType) t).getActualTypeArguments()[0];
    }
}
