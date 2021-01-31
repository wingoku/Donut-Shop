package com.wingoku.market.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.data.util.Pair;

import com.wingoku.market.models.Order;

public class Shop {

    /*
        ClientIDs: 0->20000
        Premium Client: 0->1000

        Order sorted based on timestamp (seconds)
        Preimum customer order have higher priority

        Queue Lookup timer of 5 minutes each
        Shipping cart can hold 50 donuts
     */
    private Order dummyOrder;
    private PriorityQueue<Order> ordersQueue;
    private HashMap<Integer, Order> ordersMap; //adding map to access an order based on customerID in O(1)/constant time. Trading space complexity for time complexity.
    private ShopComparator shopComparator;
    
    public Shop() {
        dummyOrder = new Order(Integer.MIN_VALUE, Long.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        ordersMap = new HashMap<>();
        shopComparator = new ShopComparator();

        ordersQueue = new PriorityQueue<Order>(shopComparator);
    }

    public Order addOrder(int customerID, int quantity) throws IllegalArgumentException {
        if(ordersMap.containsKey(customerID))
            throw new IllegalArgumentException("Order for customer id: "+ customerID + " already exists");

        if(quantity <= 0)
            throw new IllegalArgumentException("Quantity can't be less than or equal to zero");

        Order order = new Order(System.currentTimeMillis(), customerID, quantity);
        addOrder(order);
        
        return order;
    }

    private void addOrder(Order order) {
    	//O(logN)
        ordersQueue.offer(order);
        
        //O(1)
        ordersMap.putIfAbsent(order.getCustomerID(), order);
    }

    public List<Order> getOrdersList(int maxQuantity) {
        List<Order> ordersList = new ArrayList<>();

        //return empty list since no orders in the queue atm
        if(ordersQueue.isEmpty())
            return ordersList;

        int dispatchQuantity = 0;

        //O(N log K) where K is number of items pulled from pq based on the maxQuantity capacity
        //ensure that total quantity in returned orders doesn't spill over the maxQuantity provided by the call
        while(!ordersQueue.isEmpty() && dispatchQuantity + ordersQueue.peek().getQuantity() < maxQuantity) {
            Order order = ordersQueue.poll();
            ordersList.add(order);
            dispatchQuantity += order.getQuantity();
            ordersMap.remove(order.getCustomerID());
        }

        return ordersList;
    }

    public Order getNextOrder() {
        if(ordersQueue.isEmpty())
            return dummyOrder;

        //O(log N)
        return ordersQueue.poll();
    }
    
    public Order getDummyOrder() {
    	return dummyOrder;
    }
    
    //expensive method since we're calling getAllOrders() which costs O(N log N). 
    //Maybe we need to create a custom priorityQueue to support indexes. Standard Java PQ doesn't support indexes
    public Pair<Integer, Order> getPositionInQueue(int customerID) {
    	List<Order> ordersList = getAllOrders();

    	for(int i=0; i<ordersList.size(); i++) {
    		Order order = ordersList.get(i);
    		
    		if(customerID == order.getCustomerID()) {
    			return Pair.of(i+1, order); // zero index
    		}
    	}
    	
    	return Pair.of(-1, dummyOrder); //customer id doesn't exist
    }
    
    public List<Order> getAllOrderWithWaitTime() {
    	List<Order> ordersList = getAllOrders();
    	
    	double totalTimeSofar = 0.0;
    
    	//O(N)
    	for(Order order : ordersList) {
    		double waitTime = calculateWaitTime(order.getQuantity(), totalTimeSofar);
    		totalTimeSofar += waitTime;
    		order.setWaitTime(waitTime);
    	}
    	
    	return ordersList;
    }
    
    private double calculateWaitTime(int donuts, double waitTimeInQueue) {
    	return (5.0*donuts/60.0) + waitTimeInQueue; // for demo purposes I'm assuming that each donut would take 5 seconds to make. waitTimeInQueue comes from the totalWait time before this person's order
    }
    
    public List<Order> getAllOrders() {
    	//O(N)
    	List<Order> orders = Arrays.asList(ordersQueue.toArray(new Order[ordersQueue.size()]));
    	
    	//O(N log N)
    	Collections.sort(orders, shopComparator);
    	
    	return orders;
    }

    public boolean cancelOrder(int customerID) {
        if(!ordersMap.containsKey(customerID))
            return false;

        Order orderToCancel = ordersMap.get(customerID);

        //O(1)
        ordersMap.remove(customerID);
        ordersQueue.remove(orderToCancel); //Takes O(N) to remove. Next best option is Custom TreeMap (default treeMaps don't allow sorting based on the value only on keys) or customer Heap

        return true;
    }
}
