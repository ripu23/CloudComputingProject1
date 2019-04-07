package com.cloud.app.utility;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataStorage {
	
	private static long counter = 0;
	
	@Bean
	public Map<String, String> getMap(){
		Map<String, String> requestResponseMap = new ConcurrentHashMap<>();
		return requestResponseMap;
	}
	
	public long getCounter() {
		return counter++;
	}
}
