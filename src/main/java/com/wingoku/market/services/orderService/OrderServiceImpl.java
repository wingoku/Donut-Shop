package com.wingoku.market.services.orderService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.wingoku.market.core.Shop;
import com.wingoku.market.models.Order;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IOrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	IOrderRepository orderRepository;
	
	private Shop shop;

	public OrderServiceImpl() {
		shop = new Shop();
	}
	
	@Override
	public <T> ResponseBody add(T ord) {
		if (!(ord instanceof Order)) 
			throw new IllegalArgumentException("Expecting argument of type Order");
		
		Order order = (Order) ord;
		
		Pair<Order, ResponseBody> pair = addOrder(order);
		
		Order createdOrder = pair.getFirst();
		ResponseBody response = pair.getSecond();
		
		log.info("add response: "+response.isSuccess() + " tiem: "+createdOrder.getOrderTimeStamp());
		if(createdOrder != null && response.isSuccess() && orderRepository.save(createdOrder) != null)
			return new ResponseBody(true, "Order for customerID: "+createdOrder.getCustomerID()+ " added successfully", createdOrder);
		
		return response;
	}

	private Pair<Order, ResponseBody> addOrder(Order order) {
		final int customerID = order.getCustomerID();
		final int quantity = order.getQuantity();
		
		boolean addedSuccessfully = false;
		String message = "";
		Order createdOrder = null;
		
		try {
			createdOrder = shop.addOrder(customerID, quantity);
			addedSuccessfully = true;
			message = "Order added successfully for customer: " + customerID;
		}
		catch (Exception e) {
			e.printStackTrace();

			message = "Order failed for customer: " + customerID + " Reason: "+ e.getMessage();
		}
		return Pair.of(createdOrder, new ResponseBody(addedSuccessfully, message, createdOrder));
	}
	
}
