package be.virtualsushi.jfx.dorse.fxml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Explicitly sets fxml file for {@link PackageBasedUiBinder}
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface FxmlFile {

	String value() default "";

}
