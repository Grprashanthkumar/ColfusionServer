package edu.pitt.sis.exp.colfusion.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Gsonizer {
	private static final GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls();
	private static Gson gson = gsonBuilder.create();
	private static final GsonBuilder gsonBuilderAll = new GsonBuilder().serializeNulls();
	private static Gson gsonAll = gsonBuilderAll.create();
	
	public static final Gsonizer instance = new Gsonizer();
	
	public static void registerTypeAdapter(final Class<?> clazz, final Object jsonSerializer) {
		gsonBuilder.registerTypeAdapter(clazz, jsonSerializer);
		gson = gsonBuilder.create();
		
		gsonBuilderAll.registerTypeAdapter(clazz, jsonSerializer);
		gsonAll = gsonBuilderAll.create();
	}
	
	public static String toJson(final Object o, final boolean excludeNotExposed) {
		if (excludeNotExposed) {
			return gson.toJson(o);
		}
		else {
			return gsonAll.toJson(o);
		}
	}
	
	public static <T> T fromJson(final String json, final Class<T> clazz) {
		return gsonAll.fromJson(json, clazz);
	}
}
