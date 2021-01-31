package com.wingoku.market.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wingoku.market.models.Order;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.services.orderService.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/order")
public class OrderController extends BaseController {
	@Autowired
	OrderService orderService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addOrder(@RequestBody Order order) {
		log.info("---> inside addOrdedr");
		return getResponseEntityForResponseBody(orderService.add(order));
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllOrders() {
		log.info("---> inside getAllOrders");
		return getResponseEntityForResponseBody(orderService.getAllOrders());
	}
	
	@GetMapping("/position/customerID/{customerID}")
	public ResponseEntity<?> getOrderPosition(@PathVariable int customerID) {
		log.info("---> inside getOrderPosition");
		return getResponseEntityForResponseBody(orderService.getOrderPosition(customerID));
	}
	
	@GetMapping("/next")
	public ResponseEntity<?> getNextOrder() {
		log.info("---> inside getNextOrder");
		return getResponseEntityForResponseBody(orderService.getNextOrder());
	}
	
	@GetMapping("/all/waitTime")
	public ResponseEntity<?> getAllOrdersWitWaitTime() {
		log.info("---> inside getAllOrdersWitWaitTime");
		return getResponseEntityForResponseBody(orderService.getAllOrdersWitWaitTime());
	}

	@DeleteMapping("/cancel/customerID/{customerID}")
	public ResponseEntity<?> cancelOrder(@PathVariable int customerID) {
		log.info("---> inside cancelOrder::: "+ customerID);
		return getResponseEntityForResponseBody(orderService.cancel(customerID));
	}
}
