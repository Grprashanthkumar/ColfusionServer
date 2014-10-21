package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import java.io.Serializable;
import java.lang.reflect.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class CellDeserializer implements JsonDeserializer<Cell>{

	private static final Logger logger = LogManager.getLogger(CellDeserializer.class.getName());
	
	@Override
	public Cell deserialize(final JsonElement json, final Type typeOfT,
			final JsonDeserializationContext context) throws JsonParseException {
		try {
			String className = json.getAsJsonObject().get("isA").getAsString();
			
			logger.info(String.format("About to deserialize Cell from '%s' json for className '%s'", json.toString(), className));
			
			Class<?> clazz = Class.forName(className);
			
			Serializable value = context.deserialize(json.getAsJsonObject().get("value"), clazz);
			 
			return new Cell(value);
		} catch (ClassNotFoundException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
}
