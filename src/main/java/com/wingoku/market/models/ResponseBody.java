package com.wingoku.market.models;

import lombok.Data;

@Data
public class ResponseBody {
	private boolean isSuccess;
	private String message;
	private Object data;
	
	public ResponseBody(boolean isSuccess, String message) {
		this.isSuccess = isSuccess;
		this.message = message;
	}

	public ResponseBody(boolean isSuccess, String message, Object data) {
		this.isSuccess = isSuccess;
		this.message = message;
		this.data = data;
	}
}
