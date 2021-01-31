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

}
