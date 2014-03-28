package com.integration;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GSONConverter {

	private static Gson GSON = new Gson();
	
	public static String toJson(Map<String, List<Purchase>> purchases) {
		return GSON.toJson(purchases);
	}

	public static Map<String, List<Purchase>> fromJson(String purchases) {
		return GSON.fromJson(purchases, new TypeToken<Map<String, List<Purchase>>>() {}.getType());
	}
	
}
