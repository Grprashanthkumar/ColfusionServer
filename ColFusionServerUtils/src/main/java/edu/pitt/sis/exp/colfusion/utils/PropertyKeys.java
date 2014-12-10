package edu.pitt.sis.exp.colfusion.utils;

/**
 * This class is used to collect all properties for the whole colfusion system. 
 * These properties can be specified either via the config.properties file 
 * or via system properties passed to JVM.
 * 
 * @author Evgeny
 *
 */
public class PropertyKeys {
	
	
	//******************************************************************************
	// The following public static fields should be used to look up property values.
	// The key values should correspond to property keys in config.properties file.
	//*******************************************************************************
	
	/**
	 * This is used only for testing.
	 */
	public static String COLFUSION_PROPERTIES_SOURCE = "colfusion.properties.source";
	
	/**
	 * This is used only for testing.
	 */
	public static String COLFUSION_PROPERTIES_TEST_NEVER_EXISTING_PROPERTY = "colfusion.properties.test.never.existing.property";
	
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
	
	/*Appended by Hao Bai
	 * This value is used for setting timeout for connecting services 
	 * in ServiceMonitor.java of colfusionserverservicemonitor project
	*/
	public static String ServiceMonitorTimeOut = "ServiceMonitorTimeOut";
	/*Appended by Hao Bai
	 * This value is used for setting period for monitoring services 
	 * in ServiceMonitorController.java of colfusionserverservicemonitor project
	*/
	public static String ServiceMonitorPeriod = "ServiceMonitorPeriod";
	/*Appended by Hao Bai
	 * This value is used for setting email sender's address.
	*/
	public static String emailSenderAddress = "emailSenderAddress";
	/*Appended by Hao Bai
	 * This value is used for setting email sender's password.
	*/
	public static String emailSenderPassword = "emailSenderPassword";
	/*Appended by Hao Bai
	 * This value is used for setting email smtp starttls boolean value.
	*/
	public static String smtpStarttlsEnable = "smtpStarttlsEnable";
	/*Appended by Hao Bai
	 * This value is used for setting email smtp authentication boolean value.
	*/
	public static String smtpAuth = "smtpAuth";
	/*Appended by Hao Bai
	 * This value is used for setting email smtp server host.
	*/
	public static String smtpHost = "smtpHost";
	/*Appended by Hao Bai
	 * This value is used for setting email smtp port number.
	*/
	public static String smtpPort = "smtpPort";
	/*Appended by Hao Bai
	 * This value is used for setting email receiver's userLevel.
	*/
	public static String userLevel = "userLevel";
	
	
	
	
	//******* Hibernate properties (don't rename these properties) *************//
	public static String HIBERNATE_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";
	
	public static String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
	
	public static String HIBERNATE_DEFAULT_CATALOG = "hibernate.default_catalog";
	
	public static String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
			
	public static String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
	
	public static String HIBERNATE_DIALECT = "hibernate.dialect";
	
	public static String HIBERNATE_CONNECTION_ZERO_DATE_TIME_BEHAVIOR = "hibernate.connection.zeroDateTimeBehavior";
		
	public static String HIBERNATE_C3P0_MIN_SIZE = "hibernate.c3p0.min_size";
	
	public static String HIBERNATE_C3P0_MAX_SIZE = "hibernate.c3p0.max_size";
	
	public static String HIBERNATE_C3P0_TIMEOUT = "hibernate.c3p0.timeout";
	
	public static String HIBERNATE_C3P0_MAX_STATEMENTS = "hibernate.c3p0.max_statements";
	
	public static String HIBERNATE_C3P0_IDLE_TEST_PERIOD = "hibernate.c3p0.idle_test_period";
	
	public static String HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS = "current_session_context_class";
	
	public static String HIBERNATE_SHOW_SQL = "show_sql";
	
	public static String HIBERNATE_FORMAT_SQL = "format_sql";
	
	//***** END OF HIBERNATE PROPERTIES *****************//
	
}
