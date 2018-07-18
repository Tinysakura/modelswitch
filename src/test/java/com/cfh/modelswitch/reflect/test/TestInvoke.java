package com.cfh.modelswitch.reflect.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

/**
 * 测试反射机制
 * @author Mr.Chen
 * date: 2018年7月18日 上午9:32:53
 */
public class TestInvoke {
	@Test
	public void testInvoke(){
		Method[] methods = Person.class.getMethods();
		Person person = new Person();
		person.setSex("男");
		person.setAge(18);
		person.setName("陈瓜皮");
		
//		for(Method method:methods){
//			try {
//				if(method.getName().startsWith("get")){
//					System.out.println(method.invoke(person, null));	
//				}
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		Class<Person> personClass = Person.class;
		Field[] fields = Person.class.getDeclaredFields();
		for(Field field:fields){
			try {
				try {
					System.out.println(personClass.getMethod("get"+upperInitialCase(field.getName())).invoke(person));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 使用反射填充一个对象
	 */
	@Test
	public void fillEmptyObject(){
		Person person = new Person();
		person.setSex("男");
		person.setAge(18);
		person.setName("陈瓜皮");
		
		//被填充对象
		Student student = new Student();
		
		Class<Person> personClass = Person.class;
		Class<Student> studentClass = Student.class;
		
		Field[] fields = personClass.getDeclaredFields();
	
	    for(Field field:fields){
	    	try {
				Method set = studentClass.getMethod("set"+upperInitialCase(field.getName()),field.getType());
			    Method get = personClass.getMethod("get"+upperInitialCase(field.getName()));
			    
			    try {
					set.invoke(student, get.invoke(person));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
	    }
	    
	    System.out.println(student.toString());
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
