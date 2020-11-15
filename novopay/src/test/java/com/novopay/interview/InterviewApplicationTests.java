package com.novopay.interview;

import  org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class InterviewApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(InterviewApplicationTests.class);


	public static final String CONTENTTYPE_JSON = "application/json";
	public static final String UTF_8 = "UTF-8";
	public static final String CONTENTTYPE = "Content-Type";
	private static Map<String, String> headerMap = new HashMap<>();


	@Test
	void addCustomer() throws JSONException {
		String url ="http://localhost:8089/novopay/addCustomer";
		String message = "{\n" +
				"\t\"id\": 104,\n" +
				"\t\"name\": \"arun singh\",\n" +
				"\t\"accountNumber\": 512324,\n" +
				"\t\"giftBalance\": 10.00,\n" +
				"\t\"giftCardBalance\": 230.00\n" +
				"}";
		headerMap.put(CONTENTTYPE, CONTENTTYPE_JSON);

	}

}
