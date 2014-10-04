package edu.pitt.sis.exp.colfusion.dataModels.tableDataModel;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class RowSerializer implements JsonSerializer<Row> {

	@Override
	public JsonElement serialize(final Row row, final Type typeOfSrc,
			final JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		
		JsonElement jsonColumnGroups = context.serialize(row.toArray());
		jsonObject.add("columnGroups", jsonColumnGroups);
		
		return jsonObject;
	}

}
