package edu.pitt.sis.exp.colfusion.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.ColumnGroup;
import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.ColumnGroupSerializer;
import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Row;
import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.RowSerializer;
import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.TableSerializer;

public class Gsonizer {
	public static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	public static final Gson gsonAll;
	
	static {
		
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Table.class, new TableSerializer());
			gsonBuilder.registerTypeAdapter(Row.class, new RowSerializer());
			gsonBuilder.registerTypeAdapter(ColumnGroup.class, new ColumnGroupSerializer());
			
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
