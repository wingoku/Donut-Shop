package com.wingoku.market.services.base;

import com.wingoku.market.models.ResponseBody;

public interface BaseService {
	/*
	 * 
	 * In case of multiple services, common methods should go here
	 * 
	 * For demo purposes I've added add & cancel here
	 */
	public <T> ResponseBody add(T add);
	public <T> ResponseBody cancel(T remove);
}
