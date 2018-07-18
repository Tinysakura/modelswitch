package com.cfh.modelswitch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * vo注解
 * @author Mr.Chen
 * date: 2018年7月18日 上午10:51:47
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Vo {
	public Class<? extends Object> pojo();
}
