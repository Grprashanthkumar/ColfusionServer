package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CellSerializer implements JsonSerializer<Cell>{

	@Override
	public JsonElement serialize(final Cell src, final Type typeOfSrc,
			final JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		
		if (src.getValue() == null) {
			return null;
		}
		
		jsonObject.addProperty("value", src.toString());
		jsonObject.addProperty("isA", src.getValue().getClass().getName());
		
		return jsonObject;
	}
}
