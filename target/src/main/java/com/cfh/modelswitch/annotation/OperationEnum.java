package com.cfh.modelswitch.annotation;

/**
 * 对集合中元素进行操作的类型的枚举
 * @author Mr.Chen
 * date: 2018年7月18日 上午11:46:28
 */
public enum OperationEnum {
	//加
	ADD,
	//减
	SUBSTRACT,
	//乘
	MUTIPLY,
	//除
	DIVIDE,
	//指定属性一一映射
	ONE_TO_ONE,
	//字符串拼接
	STRING_SPLIT_JOIN
}