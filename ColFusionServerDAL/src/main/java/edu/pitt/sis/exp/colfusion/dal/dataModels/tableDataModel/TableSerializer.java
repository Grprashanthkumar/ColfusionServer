package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TableSerializer implements JsonSerializer<Table> {

	@Override
	public JsonElement serialize(final Table table, final Type typeOfSrc,
			final JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		
		JsonElement jsonRows = context.serialize(table.getRows().toArray());
		jsonObject.add("rows", jsonRows);
		
		return jsonObject;
	}

}
