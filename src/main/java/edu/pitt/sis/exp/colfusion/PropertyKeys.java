package edu.pitt.sis.exp.colfusion;

public class PropertyKeys {
	
	
	//******************************************************************************
	// The following public static fields should be used to look up property values.
	// The key values should correspond to property keys in config.properties file.
	//*******************************************************************************
	
	//******************************************************************************
	//
	// Keys for Production
	//
	//******************************************************************************
	
	public static String uploadRawFileLocationKey = "uploadRawFilesBaseLocation";
	
	/**
	 * Key for the property of location of the ktr base directory where newly created ktr should be put.
	 */
	public static String ktrFielsBaseLocation = "ktrFilesBaseLocation";
	
	/**
	 * Key for the property of ktr template file location for transforming csv to database 
	 */
	public static String csvToDatabaseKTRTemplate = "csv-to-database_KTR_Template";
	
	/**
	 * Key for the property of ktr template file location for transforming excel to database 
	 */
	public static String excelToDatabaseKTRTemplate = "excel-to-database_KTR_Template";
}
