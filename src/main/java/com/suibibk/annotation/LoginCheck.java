package com.suibibk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 若是方法上加了这个注解，就表明该方法是需要判断权限的，只能用在Controller上面 @AuthCheck
 * @author lwh
 *
 */
@Target({ElementType.METHOD})//只能用在方法上面
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginCheck {
}
