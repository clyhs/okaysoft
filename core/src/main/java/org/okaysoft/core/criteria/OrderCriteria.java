package org.okaysoft.core.criteria;

import java.util.LinkedList;



public class OrderCriteria {
	
	private LinkedList<Order> orders=new LinkedList<Order>();

	public LinkedList<Order> getOrders() {
		return orders;
	}

	public void addOrder(Order order) {
		this.orders.add(order);
	}

}
