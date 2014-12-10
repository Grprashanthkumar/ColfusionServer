package edu.pitt.sis.exp.colfusion.utils;

public class PropertyKeysTest {
	
	/*
	 We used have this test configs:
	  #the id of the sourceinfo to use in the tests
testStoryTitle=UnitTestStory
testUserLogin=UnitTester

# Used for testing IOUtils for uploading files. 
testUploadRawFilesBaseLocation=upload_raw_data/test

# Used for testing IOUtils for ktr files.
testKtrFilesBaseLocation=temp/test

testExcelFileNameInResourceFolder=testExcelFile.xlsx
testCSVFileNameInResourceFolder=testCSVFile.csv
testTarGzArchiveFileNameInResourceFolder=testTarGzArchive.tar.gz
testZipArchive=testZipArchive.zip

#Target databases:
#File To DB database - used to test database and table creation
testTargetFileToDBDatabase_DatabaseNamePrefix=colfusion_fileToDB_TEST
testTargetFileToDBDatabase_UserName=dataverse
testTargetFileToDBDatabase_Password=dataverse
testTargetFileToDBDatabase_Server=localhost
testTargetFileToDBDatabase_Port=3306
testTargetFileToDBDatabase_Type=mysql
	 */
	
	
	public static String colfusionURL = "colfusionURL";
	
	public static String colfusionRootLocation = "colfusionRootLocation";
	
	public static String testStoryTitle = "testStoryTitle";
	
	public static String test = "test";
	
	public static String testExcelFileNameInResourceFolder = "testExcelFileNameInResourceFolder";
	
	public static String testCSVFileNameInResourceFolder = "testCSVFileNameInResourceFolder";
	
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

	public static String testUserLogin = "testUserLogin";
}
