package com.wingoku.market.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="donut_orders")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private long orderTimeStamp;
    private int customerID;
    private int quantity;
    private double waitTime;
    
    public Order() {}

    public Order(int customerID, int quantity) {
        this.customerID = customerID;
        this.quantity = quantity;
    }
    
    public Order(long orderTimeStamp, int customerID, int quantity) {
        this.orderTimeStamp = orderTimeStamp;
        this.customerID = customerID;
        this.quantity = quantity;
    }
    
    public Order(int id, long orderTimeStamp, int customerID, int quantity) {
        this.id = id;
        this.orderTimeStamp = orderTimeStamp;
        this.customerID = customerID;
        this.quantity = quantity;
    }
}
