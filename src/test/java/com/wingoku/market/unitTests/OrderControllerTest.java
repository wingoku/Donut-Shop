package com.wingoku.market.unitTests;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wingoku.market.controllers.OrderController;
import com.wingoku.market.models.Order;
import com.wingoku.market.models.ResponseBody;
import com.wingoku.market.services.orderService.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderController.class)
@WithMockUser
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;
	private static ObjectWriter ow;

	private static ResponseBody mockResponseBody;
	private static  List<Order> mockOrderList;
	private static int orderIndex = 0;
	private static Random rand;

	@BeforeAll
	public static void init() {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ow = mapper.writer().withDefaultPrettyPrinter();
	    
	    //Order(int id, long orderTimeStamp, int customerID, int quantity)
	    
	    rand = new Random();
	    mockOrderList = new ArrayList<>();

	    for(int i=0; i<5; i++) {
	    	int customerID1 = rand.nextInt(1000);
	    	int customerID2 = customerID1 + rand.nextInt(19000);
	    	mockOrderList.add(new Order(orderIndex++, System.currentTimeMillis(), customerID1, rand.nextInt(20)));
	    	mockOrderList.add(new Order(orderIndex++, System.currentTimeMillis(), customerID2, rand.nextInt(20)));
	    }
	}

	@Test
	public void addOrderTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "Order for customerID: {} added successfully");
		Order mockOrder = new Order(orderIndex++, System.currentTimeMillis(), rand.nextInt(19000), rand.nextInt(20));
		Mockito.when(orderService.add(mockOrder)).thenReturn(mockResponseBody);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/order/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsBytes(mockOrder))
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		log.info("----> TEST::addOrderTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockResponseBody);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void cancelOrderTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "Order cancellation for customer: 1");
		mockResponseBody.setData(null);
		Order mockOrder = mockOrderList.size() > 0 ? mockOrderList.get(mockOrderList.size()-1) : new Order();
		Mockito.when(orderService.cancel(mockOrder.getCustomerID())).thenReturn(mockResponseBody);
		
		System.out.println("cancel id: "+ mockOrder.getCustomerID());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/order/cancel/customerID/"+mockOrder.getCustomerID())
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		log.info("----> TEST::cancelOrderTest(): response: {}", result.getResponse().getContentAsString());

		String expected = ow.writeValueAsString(mockResponseBody);
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}

	@Test
	public void getAllOrdersTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "");
		mockResponseBody.setData(mockOrderList);
		Mockito.when(orderService.getAllOrders()).thenReturn(mockResponseBody);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/order/all")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		log.info("----> TEST::getAllOrdersTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockResponseBody);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void getOrderPositionTest() throws Exception {
		ResponseBody mockResponseBody = new ResponseBody(true, "Order cancellation for customer: {}");
		Order mockOrder = mockOrderList.size() > 0 ? mockOrderList.get(mockOrderList.size()-1) : new Order();
		mockResponseBody.setData(1);
		Mockito.when(orderService.getOrderPosition(mockOrder.getCustomerID())).thenReturn(mockResponseBody);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/order/position/customerID/"+mockOrder.getCustomerID())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		log.info("----> TEST::getOrderPositionTest(): response: {}", result.getResponse().getContentAsString());
		String expected = ow.writeValueAsString(mockResponseBody);

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
}
