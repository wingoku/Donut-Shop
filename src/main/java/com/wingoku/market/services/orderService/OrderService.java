package com.wingoku.market.services.orderService;

import java.util.List;

import com.wingoku.market.models.Order;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.respositories.interfaces.IOrderRepository;
import com.wingoku.market.services.base.BaseService;

public interface OrderService extends BaseService{
	public ResponseBody getNextOrder();
	public ResponseBody getAllOrders();
	public ResponseBody getAllOrdersWitWaitTime();
	public ResponseBody getOrderPosition(int customerID);
}
