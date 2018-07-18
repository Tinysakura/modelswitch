package com.cfh.modelswitch.annotation.test;

import java.util.List;

import com.cfh.modelswitch.annotation.FieldTypeEnum;
import com.cfh.modelswitch.annotation.Mapped;
import com.cfh.modelswitch.annotation.OperationEnum;
import com.cfh.modelswitch.annotation.Vo;

@Vo(pojo=Order.class)
public class OrderVo {
	Integer orderNo;
	@Mapped(value="userId")
	Integer userid;
	List<OrderItem> orderItems;
	@Mapped(value="orderItems",isCollection=true,type=OrderItem.class,
	field="productPriece",operation=OperationEnum.ONE_TO_ONE,fieldType=FieldTypeEnum.ARRAY_LIST)
	//订单中所有orderItem的总价,是一个推算字段无法直接映射
	List<Integer> totalPriece;
	
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public List<Integer> getTotalPriece() {
		return totalPriece;
	}
	public void setTotalPriece(List<Integer> totalPriece) {
		this.totalPriece = totalPriece;
	}
}
