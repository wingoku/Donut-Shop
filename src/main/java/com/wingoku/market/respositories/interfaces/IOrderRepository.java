package com.wingoku.market.respositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wingoku.market.models.Order;


public interface IOrderRepository extends JpaRepository<Order, Integer> {
	public Order findByCustomerID(int customerID);
}
