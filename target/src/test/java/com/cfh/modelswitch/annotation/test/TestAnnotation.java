package com.cfh.modelswitch.annotation.test;

import java.util.ArrayList;

import org.junit.Test;

import com.cfh.modelswitch.processor.impl.VoProcessorImpl;

/**
 * 测试自定义的注解
 * @author Mr.Chen
 * date: 2018年7月18日 上午11:07:05
 */
public class TestAnnotation {
	@Test
	public void test(){
		Order order = new Order();
		order.setOrderNo(1);
		order.setUserId(1);
		
		OrderItem orderItem = new OrderItem();
		orderItem.setProductId(1);
		orderItem.setProductAmount(2);
		orderItem.setProductPriece(200);
		orderItem.setTotalPriece(400);
		
		OrderItem orderItem2 = new OrderItem();
		orderItem2.setProductId(2);
		orderItem2.setProductAmount(3);
		orderItem2.setProductPriece(100);
		orderItem2.setTotalPriece(300);
		
		ArrayList<OrderItem> list = new ArrayList<OrderItem>();
		list.add(orderItem);
		list.add(orderItem2);
		
		order.setOrderItems(list);
		
		VoProcessorImpl processor = new VoProcessorImpl();
		try {
			OrderVo orderVo = (OrderVo)processor.process(OrderVo.class,order);
			System.out.println(orderVo.getTotalPriece());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
