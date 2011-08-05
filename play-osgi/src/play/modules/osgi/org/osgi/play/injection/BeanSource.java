package org.osgi.play.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;

public interface BeanSource {

    public <T> T getBeanOfType(Class<T> clazz, Class<?> from, Member m, Annotation... qualifiers);

    public <T> Iterable<T> getBeanCollectionOfType(Class<T> clazz, Class<?> from, Member m, Annotation... qualifiers);

}
