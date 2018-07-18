package com.cfh.modelswitch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 指定Vo类中需要填充的字段与pojo中字段的对应关系
 * @author Mr.Chen
 * date: 2018年7月18日 上午11:34:21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapped {
	//映射对应的属性名
	public String value();
	//映射的字段是否是集合
	public boolean isCollection() default false;
	//集合中元素的类型
	public Class<? extends Object> type() default Object.class;
	//集合类型中用于运算的字段
	public String field() default "";
	//运算的类型
	public OperationEnum operation() default OperationEnum.ADD;
	//字段的类型
	public FieldTypeEnum fieldType() default FieldTypeEnum.INT;
}
