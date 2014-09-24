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
	
	public static String colfusionURL = "colfusionURL";
	
	public static String colfusionRootLocation = "colfusionRootLocation";
	
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

	
	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * DatabaseName is the name of the database where the data should be loaded.
	 */
	public static String targetFileToDBDatabase_DatabaseNamePrefix = "targetFileToDBDatabase_DatabaseNamePrefix";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * UserName is the name user which need to be used to connect to the database.
	 */
	public static String targetFileToDBDatabase_UserName = "targetFileToDBDatabase_UserName";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Password is the password which need to be used to connect to the database.
	 */
	public static String targetFileToDBDatabase_Password = "targetFileToDBDatabase_Password";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Server is the URL of the server on which the database is running.
	 */
	public static String targetFileToDBDatabase_Server = "targetFileToDBDatabase_Server";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Port is the port of the server on which the database is running.
	 */
	public static String targetFileToDBDatabase_Port = "targetFileToDBDatabase_Port";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Type is the database vendor, e.g. MySQL, MSSQL, etc.
	 */
	public static String targetFileToDBDatabase_Type = "targetFileToDBDatabase_Type";
	
	
	
	
	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * DatabaseName is the name of the database where the data should be loaded.
	 */
	public static String logginDatabase_DatabaseNamePrefix = "logginDatabase_DatabaseNamePrefix";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * UserName is the name user which need to be used to connect to the database.
	 */
	public static String logginDatabase_UserName = "logginDatabase_UserName";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Password is the password which need to be used to connect to the database.
	 */
	public static String logginDatabase_Password = "logginDatabase_Password";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Server is the URL of the server on which the database is running.
	 */
	public static String logginDatabase_Server = "logginDatabase_Server";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Port is the port of the server on which the database is running.
	 */
	public static String logginDatabase_Port = "logginDatabase_Port";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Type is the database vendor, e.g. MySQL, MSSQL, etc.
	 */
	public static String logginDatabase_Type = "logginDatabase_Type";
	
	
	/**
	 * Carte Server URL to use for KTR transformation execution.
	 */
	public static String carteServerURL = "carteServerURL";
	public static String carteServer = "carteServer";
	public static String cartePort = "cartePort";
	public static String carteUser = "carteUser";
	public static String cartePassword = "cartePassword";
	
	
	
	
	
	
	public static String linkedServerDatabaseName = "linkedServerDatabaseName";
	public static String linkedServerUserName = "linkedServerUserName";
	public static String linkedServerPassword = "linkedServerPassword";
	public static String linkedServerServer = "linkedServerServer";
	public static String linkedServerPort = "linkedServerPort";
	
	
	public static String linkedServerColFusionHost = "linkedServerColFusionHost";

	public static String maxNumberOfConcurrentProceses = "maxNumberOfConcurrentProceses";
	
}
