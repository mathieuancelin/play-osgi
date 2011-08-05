package org.osgi.play.injection;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.*;

@Target({ METHOD, PARAMETER, FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface OSGiService {
    String value() default "";
}
