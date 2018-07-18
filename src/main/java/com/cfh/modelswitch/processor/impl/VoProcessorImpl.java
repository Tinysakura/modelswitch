package com.cfh.modelswitch.processor.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.cfh.modelswitch.annotation.Mapped;
import com.cfh.modelswitch.annotation.Vo;
import com.cfh.modelswitch.processor.VoProcessor;

public class VoProcessorImpl implements VoProcessor{

	public Object process(Class<? extends Object> vo, Object pojo) throws Exception{
		Vo voAnnotation = getVoInterface(vo);
		
		if(voAnnotation == null){
			throw new RuntimeException("vo类上没有相关的注解，请检查");
		}
		
		if(voAnnotation.pojo() != pojo.getClass()){
			throw new RuntimeException("不是正确的映射关系请检查传入的pojo是否为指定类型");
		}
		
		//扫描Vo类字段上的mapped注解，对vo的字段进行填充
		return mappedProcess(vo,pojo);
	}
	
	/**
	 * 判断指定类上是否有Vo注解
	 */
	private Vo getVoInterface(Class<? extends Object> classInfo){
		Annotation[] annotations = classInfo.getAnnotations();
		
		for(Annotation annotation:annotations){
			if(annotation.annotationType() == Vo.class){
				return (Vo) annotation;
			}
		}
		
		return null;
	}
	
	/** 
	 * 根据mapped注解提供的信息进行字段填充
	 */
	/**
	 * @param vo
	 * @param pojo
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private Object mappedProcess(Class<? extends Object> vo,Object pojo) throws InstantiationException, IllegalAccessException{
		Class<? extends Object> pClass = pojo.getClass();
			
		Object voObject = vo.newInstance();
		
		Field[] fields = vo.getDeclaredFields();
		Method setMethod;
		Method getMethod;
		
		/**
		 * 遍历fields
		 * 若有mapped注解标注则尝试使用注解的value去pojo中寻找映射属性
		 * 若无mapped注解则尝试使用字段原名取pojo中寻找映射属性
		 */
		for(Field field:fields){
			Mapped mappedAnnotation = field.getAnnotation(Mapped.class);
			
			if(mappedAnnotation == null){
				try {
					setMethod = vo.getMethod("set"+upperInitialCase(field.getName()),field.getType());
					getMethod = pClass.getMethod("get"+upperInitialCase(field.getName()));
					setMethod.invoke(voObject, getMethod.invoke(pojo));
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				/**
				 * mapped注解不为空的情况，检查需要映射的是否是集合类型
				 */
				if(!mappedAnnotation.isCollection()){
					try {
						setMethod = vo.getMethod("set"+upperInitialCase(field.getName()),field.getType());
						getMethod = pClass.getMethod("get"+upperInitialCase(mappedAnnotation.value()));
						setMethod.invoke(voObject, getMethod.invoke(pojo));
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					//如果集合中没有要运算的字段说明只是对集合做一个简单的映射
					if(mappedAnnotation.field().equals("")){
						try {
							setMethod = vo.getMethod("set"+upperInitialCase(field.getName()),field.getType());
							getMethod = pClass.getMethod("get"+upperInitialCase(mappedAnnotation.value()));
							setMethod.invoke(voObject, getMethod.invoke(pojo));
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						/**
						 * 集合中有需要运算的字段
						 */
						switch (mappedAnnotation.operation()) {
						case ADD:
							try {
								setMethod = vo.getMethod("set"+upperInitialCase(field.getName()),field.getType());
								getMethod = pClass.getMethod("get"+upperInitialCase(mappedAnnotation.value()));
								
								//迭代集合进行相应的映射
							    Collection<? extends Object> collection = (Collection<? extends Object>) getMethod.invoke(pojo);
							    Class<? extends Object> type = mappedAnnotation.type();
							    Iterator<? extends Object> iterator = collection.iterator();
							    BigDecimal bigDecimal = new BigDecimal("0");
							    while(iterator.hasNext()){
							    	bigDecimal = bigDecimal.add(
							    			new BigDecimal(type.getMethod("get"+upperInitialCase(mappedAnnotation.field())).
							    			invoke(iterator.next()).toString()));
							    }
							    
							    switch (mappedAnnotation.fieldType()) {
								case INT:
									setMethod.invoke(voObject, bigDecimal.intValue());
									break;
								case SHORT:
									setMethod.invoke(voObject, bigDecimal.shortValue());
									break;
								case LONG:
									setMethod.invoke(voObject, bigDecimal.longValue());
									break;
								case DOUBLE:
									setMethod.invoke(voObject, bigDecimal.doubleValue());
									break;
								case FLOAT:
									setMethod.invoke(voObject, bigDecimal.floatValue());
									break;
								default:
									break;
								}
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			
							break;
						case SUBSTRACT:
							try {
								setMethod = vo.getMethod("set"+upperInitialCase(field.getName()),field.getType());
								getMethod = pClass.getMethod("get"+upperInitialCase(mappedAnnotation.value()));
								
								//迭代集合进行相应的映射
							    Collection<? extends Object> collection = (Collection<? extends Object>) getMethod.invoke(pojo);
							    Class<? extends Object> type = mappedAnnotation.type();
							    Iterator<? extends Object> iterator = collection.iterator();
							    //用第一个元素的值向后减
							    BigDecimal bigDecimal = new BigDecimal(type.getMethod("get"+upperInitialCase(mappedAnnotation.field())).
						    			invoke(iterator.next()).toString());
							    while(iterator.hasNext()){
							    	bigDecimal = bigDecimal.subtract(
							    			new BigDecimal(type.getMethod("get"+upperInitialCase(mappedAnnotation.field())).
							    			invoke(iterator.next()).toString()));
							    }
							    
							    switch (mappedAnnotation.fieldType()) {
								case INT:
									setMethod.invoke(voObject, bigDecimal.intValue());
									break;
								case SHORT:
									setMethod.invoke(voObject, bigDecimal.shortValue());
									break;
								case LONG:
									setMethod.invoke(voObject, bigDecimal.longValue());
									break;
								case DOUBLE:
									setMethod.invoke(voObject, bigDecimal.doubleValue());
									break;
								case FLOAT:
									setMethod.invoke(voObject, bigDecimal.floatValue());
									break;
								default:
									break;
								}
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
							break;
						case MUTIPLY:
							try {
								setMethod = vo.getMethod("set"+upperInitialCase(field.getName()),field.getType());
								getMethod = pClass.getMethod("get"+upperInitialCase(mappedAnnotation.value()));
								
								//迭代集合进行相应的映射
							    Collection<? extends Object> collection = (Collection<? extends Object>) getMethod.invoke(pojo);
							    Class<? extends Object> type = mappedAnnotation.type();
							    Iterator<? extends Object> iterator = collection.iterator();
							    //用第一个元素的值向后乘
							    BigDecimal bigDecimal = new BigDecimal(type.getMethod("get"+upperInitialCase(mappedAnnotation.field())).
						    			invoke(iterator.next()).toString());
							    while(iterator.hasNext()){
							    	bigDecimal = bigDecimal.multiply(
							    			new BigDecimal(type.getMethod("get"+upperInitialCase(mappedAnnotation.field())).
							    			invoke(iterator.next()).toString()));
							    }
							    
							    switch (mappedAnnotation.fieldType()) {
								case INT:
									setMethod.invoke(voObject, bigDecimal.intValue());
									break;
								case SHORT:
									setMethod.invoke(voObject, bigDecimal.shortValue());
									break;
								case LONG:
									setMethod.invoke(voObject, bigDecimal.longValue());
									break;
								case DOUBLE:
									setMethod.invoke(voObject, bigDecimal.doubleValue());
									break;
								case FLOAT:
									setMethod.invoke(voObject, bigDecimal.floatValue());
									break;
								default:
									break;
								}
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
							break;
						case DIVIDE:
							try {
								setMethod = vo.getMethod("set"+upperInitialCase(field.getName()),field.getType());
								getMethod = pClass.getMethod("get"+upperInitialCase(mappedAnnotation.value()));
								
								//迭代集合进行相应的映射
							    Collection<? extends Object> collection = (Collection<? extends Object>) getMethod.invoke(pojo);
							    Class<? extends Object> type = mappedAnnotation.type();
							    Iterator<? extends Object> iterator = collection.iterator();
							    //用第一个元素的值向后除
							    BigDecimal bigDecimal = new BigDecimal(type.getMethod("get"+upperInitialCase(mappedAnnotation.field())).
						    			invoke(iterator.next()).toString());
							    while(iterator.hasNext()){
							    	//四舍五入保留两位精度
							    	bigDecimal = bigDecimal.divide(
							    			new BigDecimal(type.getMethod("get"+upperInitialCase(mappedAnnotation.field())).
							    			invoke(iterator.next()).toString()),2,BigDecimal.ROUND_HALF_UP);
							    }
							    
							    switch (mappedAnnotation.fieldType()) {
								case INT:
									setMethod.invoke(voObject, bigDecimal.intValue());
									break;
								case SHORT:
									setMethod.invoke(voObject, bigDecimal.shortValue());
									break;
								case LONG:
									setMethod.invoke(voObject, bigDecimal.longValue());
									break;
								case DOUBLE:
									setMethod.invoke(voObject, bigDecimal.doubleValue());
									break;
								case FLOAT:
									setMethod.invoke(voObject, bigDecimal.floatValue());
									break;
								default:
									break;
								}
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
							break;
						//将pojo集合中元素的相应属性映射到新的集合中
						case ONE_TO_ONE:
							try {
								setMethod = vo.getMethod("set"+upperInitialCase(field.getName()),field.getType());
								getMethod = pClass.getMethod("get"+upperInitialCase(mappedAnnotation.value()));
								
								//迭代集合进行相应的映射
							    Collection<? extends Object> collection = (Collection<? extends Object>) getMethod.invoke(pojo);
							    Class<? extends Object> type = mappedAnnotation.type();
							    Iterator<? extends Object> iterator = collection.iterator();
							    
							    switch (mappedAnnotation.fieldType()) {
								case ARRAY_LIST:
									List<Object> list = new ArrayList<Object>();
								    while(iterator.hasNext()){
								    	list.add(type.getMethod("get"+upperInitialCase(mappedAnnotation.field())).invoke(iterator.next()));
								    }
								    setMethod.invoke(voObject, list);
									break;
								case HASH_MAP:
									//...
									break;
								case HASH_SET:
									//...
									break;
								default:
									break;
								}
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							break;
						case STRING_SPLIT_JOIN:
							try {
								setMethod = vo.getMethod("set"+upperInitialCase(field.getName()),field.getType());
								getMethod = pClass.getMethod("get"+upperInitialCase(mappedAnnotation.value()));
								
								//迭代集合进行相应的映射
							    Collection<? extends Object> collection = (Collection<? extends Object>) getMethod.invoke(pojo);
							    Class<? extends Object> type = mappedAnnotation.type();
							    Iterator<? extends Object> iterator = collection.iterator();
							    
							    StringBuilder sb = new StringBuilder();
							    while(iterator.hasNext()){
							    	sb = sb.append(type.getMethod("get"+upperInitialCase(mappedAnnotation.field())).invoke(iterator.next()).toString());
							    }
							    
							    setMethod.invoke(voObject, sb.toString());
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							break;
						}
					}
				}
			}
		}//loop_end
		
		return voObject;
	}
	
	/**
	 * 将字符串的首字母变为大写
	 * @param str
	 * @return
	 */
	public String upperInitialCase(String str) {
	    char[] ch = str.toCharArray();
	    if (ch[0] >= 'a' && ch[0] <= 'z') {
	        ch[0] = (char) (ch[0] - 32);
	    }
	    return new String(ch);
	}

}
