package com.wingoku.market.controllers;

import org.springframework.http.ResponseEntity;

import com.wingoku.market.models.ResponseBody;

public class BaseController {
	
	protected ResponseEntity<?> getResponseEntityForResponseBody(ResponseBody responseBody) {
		if(responseBody.isSuccess())
			return ResponseEntity.ok(responseBody);
		
		return ResponseEntity.badRequest().body(responseBody);
	}
	
	protected ResponseEntity<?> getSuccessResponseEntityObject(Object object) {
		return ResponseEntity.ok(object);
	}
	
	protected ResponseEntity<?> getEmptyResponseEntityObject() {
		return ResponseEntity.noContent().build();
	}
}
