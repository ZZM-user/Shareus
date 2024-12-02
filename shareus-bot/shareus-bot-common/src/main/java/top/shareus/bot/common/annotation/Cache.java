package top.shareus.bot.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface Cache {
	
	String name() default "";
	
	String key() default "";
	
	long ttl() default - 1L;
	
	TimeUnit unit() default TimeUnit.SECONDS;
}
