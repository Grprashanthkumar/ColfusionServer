package edu.pitt.sis.exp.colfusion.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;

public class Gsonizer {
	private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private static final GsonBuilder gsonBuilder = new GsonBuilder();
	private static Gson gsonAll = new GsonBuilder().create();
	
	public static final Gsonizer instance = new Gsonizer();
	
	public static void registerTypeAdapter(final Class<?> clazz, final JsonSerializer<?> jsonSerializer) {
		gsonBuilder.registerTypeAdapter(clazz, jsonSerializer);
		gsonAll = gsonBuilder.create();
	}
	
	public static String toJson(final Object o, final boolean excludeNotExposed) {
		if (excludeNotExposed) {
			return gson.toJson(o);
		}
		else {
			return gsonAll.toJson(o);
		}
	}
}
