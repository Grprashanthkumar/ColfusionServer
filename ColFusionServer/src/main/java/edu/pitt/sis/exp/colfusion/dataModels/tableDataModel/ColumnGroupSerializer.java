package edu.pitt.sis.exp.colfusion.dataModels.tableDataModel;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ColumnGroupSerializer implements JsonSerializer<ColumnGroup> {

	@Override
	public JsonElement serialize(final ColumnGroup columnGroup, final Type typeOfSrc,
			final JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("tableName", columnGroup.getTableName());
		JsonElement jsonColumns = context.serialize(columnGroup.toArray());
		jsonObject.add("columns", jsonColumns);
		
		return jsonObject;
	}

}
