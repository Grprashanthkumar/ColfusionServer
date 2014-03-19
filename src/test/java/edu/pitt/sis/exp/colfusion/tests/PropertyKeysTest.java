package edu.pitt.sis.exp.colfusion.tests;

public class PropertyKeysTest {
	
	//******************************************************************************
	// The following public static fields should be used to look up property values.
	// The key values should correspond to property keys in config.properties file.
	//*******************************************************************************
	
	//******************************************************************************
	//
	// Keys for Debug/Tests
	//
	//******************************************************************************
	
	public static String test = "test";
	
	public static String testExcelFileNameInResourceFolder = "testExcelFileNameInResourceFolder";

	public static String testUploadRawFilesBaseLocation = "testUploadRawFilesBaseLocation";
	
	public static String testTarGzArchiveFileNameInResourceFolder = "testTarGzArchiveFileNameInResourceFolder";
	
	public static String testZipArchive = "testZipArchive";
	
	
	/**
	 * Key for the property of location of the ktr base directory where newly created ktr should be put.
	 */
	public static String testKtrFielsBaseLocation = "testKtrFilesBaseLocation";
	
	/**
	 * Key for the property of ktr template file location for transforming csv to database 
	 */
	public static String testCsvToDatabaseKTRTemplate = "csv-to-database_KTR_Template";
	
	/**
	 * Key for the property of ktr template file location for transforming excel to database 
	 */
	public static String testExcelToDatabaseKTRTemplate = "excel-to-database_KTR_Template";
	
	
	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * DatabaseName is the name of the database where the data should be loaded.
	 */
	public static String testTargetFileToDBDatabase_DatabaseNamePrefix = "testTargetFileToDBDatabase_DatabaseNamePrefix";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * UserName is the name user which need to be used to connect to the database.
	 */
	public static String testTargetFileToDBDatabase_UserName = "testTargetFileToDBDatabase_UserName";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Password is the password which need to be used to connect to the database.
	 */
	public static String testTargetFileToDBDatabase_Password = "testTargetFileToDBDatabase_Password";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Server is the URL of the server on which the database is running.
	 */
	public static String testTargetFileToDBDatabase_Server = "testTargetFileToDBDatabase_Server";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Port is the port of the server on which the database is running.
	 */
	public static String testTargetFileToDBDatabase_Port = "testTargetFileToDBDatabase_Port";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Type is the database vendor, e.g. MySQL, MSSQL, etc.
	 */
	public static String testTargetFileToDBDatabase_Type = "testTargetFileToDBDatabase_Type";
}
