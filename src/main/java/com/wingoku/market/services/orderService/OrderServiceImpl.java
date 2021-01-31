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
		Order createdOrder = shop.getDummyOrder();
		
		try {
			createdOrder = shop.addOrder(customerID, quantity);
			addedSuccessfully = true;
			message = "Order added successfully for customer: " + customerID;
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			
			message = "Order failed for customer: " + customerID + " Reason: "+ e.getMessage();
		}
		return Pair.of(createdOrder, new ResponseBody(addedSuccessfully, message, createdOrder));
	}
	
	@Override
	public <T> ResponseBody cancel(T id) {
		if (!(id instanceof Integer)) 
			throw new IllegalArgumentException("Expecting argument of type Integer");

		int customerID = (int) id;
		ResponseBody response = cancelOrder(customerID);

		log.info("cancel: "+customerID + " response: "+ response.isSuccess());
		if(response.isSuccess()) {
			Order order = orderRepository.findByCustomerID(customerID);
			
			if(order != null) {
				orderRepository.delete(order);
			}
		}

		return response;
	}

	private ResponseBody cancelOrder(int customerID) {
		log.info("canceling order for customer: "+customerID);
		boolean isCancelled = shop.cancelOrder(customerID);
		return new ResponseBody(isCancelled, "Order cancellation for customer: "+ customerID);
	}

	@Override
	public ResponseBody getAllOrders() {
		System.out.print("INSIDE GET ALL ORDERSSSSS");
		List<Order> orderList = shop.getAllOrders();
		
		if(orderList.isEmpty()) {
			return new ResponseBody(false, "No more orders in queue");
		}
		
		return new ResponseBody(true, "", orderList);
	}

	@Override
	public ResponseBody getAllOrdersWitWaitTime() {
		List<Order> orderList = shop.getAllOrderWithWaitTime();
		
		if(orderList.isEmpty()) {
			return new ResponseBody(false, "No more orders in queue");
		}
		
		return new ResponseBody(true, "", orderList);
	}

	@Override
	public ResponseBody getOrderPosition(int customerID) {
		Pair<Integer, Order> pair = shop.getPositionInQueue(customerID);
		boolean isSuccess = false;
		int position = pair.getFirst();
		Order order = pair.getSecond();
		
		String message = "";
		
		if(position == -1) {
			isSuccess = false;
			message = "Order not found";
		}
		else {
			isSuccess = true;
			message = "Position in queue is "+ position;
		}
		
		return new ResponseBody(isSuccess, message, order);
	}

	@Override
	public ResponseBody getNextOrder() {
		Order order = shop.getNextOrder();
		
		if(order.getCustomerID() == Integer.MIN_VALUE) {
			return new ResponseBody(false, "Next Order Not Found. No more orders in queue");
		}
		return new ResponseBody(true, "", order);
	}
}
