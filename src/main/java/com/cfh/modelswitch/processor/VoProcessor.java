package com.cfh.modelswitch.processor;

/**
 * Vo注解的处理器
 * @author Mr.Chen
 * date: 2018年7月18日 上午11:14:59
 */
public interface VoProcessor {
	public Object process(Class<? extends Object> vo,Object pojo) throws Exception;
}
