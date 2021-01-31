package com.wingoku.market.core;

import java.util.Comparator;

import com.wingoku.market.models.Order;

public class ShopComparator implements Comparator<Order> {

	@Override
	public int compare(Order o1, Order o2) {
		//if premium customer, sort them based on their customerID -> sorted in ascending order
		if(o1.getCustomerID() < 1000 || o2.getCustomerID() < 1000)
            return Long.compare(o1.getCustomerID(), o2.getCustomerID());

		//if customer is not premium customer than order them based on the seconds difference of their order timestamp. sorted in ascending order
        return Long.compare(o1.getOrderTimeStamp(), o2.getOrderTimeStamp());
	}
}
