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
}
