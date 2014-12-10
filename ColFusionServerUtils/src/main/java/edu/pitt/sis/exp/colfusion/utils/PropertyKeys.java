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
	
	public static String COLFUSION_URL = "colfusion.url";
	
	public static String COLFUSION_STATIC_FILES_ROOT_LOCATION = "colfusion.static_files.root_location";
	
	public static String COLFUSION_UPLOAD_FILES_FOLDER = "colfusion.upload_files_folder";
	
	/**
	 * Key for the property of location of the ktr base directory where newly created ktr should be put.
	 */
	public static String COLFUSION_KTR_FOLDER = "colfusion.ktr.folder";
	
	/**
	 * Key for the property of ktr template file location for transforming csv to database 
	 */
	public static String COLFUSION_KTR_TEMPLATES_CSV_TO_DATABASE = "colfusion.ktr.templates.csv_to_database";
	
	/**
	 * Key for the property of ktr template file location for transforming excel to database 
	 */
	public static String COLFUSION_KTR_TEMPLATES_EXCEL_TO_DATABASE = "colfusion.ktr.templates.excel_to_database";

	
	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * DatabaseName is the name of the database where the data should be loaded.
	 */
	public static String COLFUSION_DATA_FROM_FILE_DATABASE_DATABASE_NAME_PREFIX = "colfusion.data.from_file_database.database_name_prefix";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * UserName is the name user which need to be used to connect to the database.
	 */
	public static String COLFUSION_DATA_FROM_FILE_DATABASE_USERNAME = "colfusion.data.from_file_database.username";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Password is the password which need to be used to connect to the database.
	 */
	public static String COLFUSION_DATA_FROM_FILE_DATABASE_PASSWORD = "colfusion.data.from_file_database.password";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Server is the URL of the server on which the database is running.
	 */
	public static String COLFUSION_DATA_FROM_FILE_DATABASE_HOST = "colfusion.data.from_file_database.host";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Port is the port of the server on which the database is running.
	 */
	public static String COLFUSION_DATA_FROM_FILE_DATABASE_PORT = "colfusion.data.from_file_database.port";

	/**
	 * Target FileToDB database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Type is the database vendor, e.g. MySQL, MSSQL, etc.
	 */
	public static String COLFUSION_DATA_FROM_FILE_DATABASE_VENDOR = "colfusion.data.from_file_database.vendor";
	
	
	
	
	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * DatabaseName is the name of the database where the data should be loaded.
	 */
	public static String COLFUSION_PENTAHO_LOGGING_DATABASE_DATABASE_NAME = "colfusion.pentaho.logging_database.database_name";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * UserName is the name user which need to be used to connect to the database.
	 */
	public static String COLFUSION_PENTAHO_LOGGING_DATABASE_USERNAME = "colfusion.pentaho.logging_database.username";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Password is the password which need to be used to connect to the database.
	 */
	public static String COLFUSION_PENTAHO_LOGGING_DATABASE_PASSWORD = "colfusion.pentaho.logging_database.password";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Server is the URL of the server on which the database is running.
	 */
	public static String COLFUSION_PENTAHO_LOGGING_DATABASE_HOST = "colfusion.pentaho.logging_database.host";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Port is the port of the server on which the database is running.
	 */
	public static String COLFUSION_PENTAHO_LOGGING_DATABASE_PORT = "colfusion.pentaho.logging_database.port";

	/**
	 * Logging database is the database where the data should actually be loaded. Currently only one target database is used.
	 * 
	 * Type is the database vendor, e.g. MySQL, MSSQL, etc.
	 */
	public static String COLFUSION_PENTAHO_LOGGING_DATABASE_VENDOR = "colfusion.pentaho.logging_database.";
	
	
	/**
	 * Carte Server URL to use for KTR transformation execution.
	 */
	public static String COLFUSION_PENTAHO_CARTE_URL = "colfusion.pentaho.carte.url";
	public static String COLFUSION_PENTAHO_CARTE_HOST = "colfusion.pentaho.carte.host";
	public static String COLFUSION_PENTAHO_CARTE_PORT = "colfusion.pentaho.carte.port";
	public static String COLFUSION_PENTAHO_CARTE_USER = "colfusion.pentaho.carte.username";
	public static String COLFUSION_PENTAHO_CARTE_PASSWORD = "colfusion.pentaho.carte.password";
	
	public static String COLFUSION_PROCESS_MANAGER_MAX_CONCURRENT_PROCESSES = "colfusion.process_manager.max_concurrent_processes";
	
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
	
	public static String HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS = "hibernate.current_session_context_class";
	
	public static String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	
	public static String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
	
	//***** END OF HIBERNATE PROPERTIES *****************//

	public static final String COLFUSION_OPENREFINE_URL = "colfusion.openrefine.url";	
	
	public static final String COLFUSION_OPENREFINE_FOLDER = "colfusion.openrefine.folder";

	public static final String COLFUSION_OPENREFINE_FOLDER_TEMP = "colfusion.openrefine.folder.temp";
	
	public static final String COLFUSION_OPENREFINE_LOCK_TIME = "colfusion.openrefine.lock_time";
	
	public static final String COLFUSION_OPENREFINE_NOTICE_TIME = "colfusion.openrefine.notice_time";
	
	public static final String COLFUSION_OPENREFINE_CSV_FILE_DIR = "colfusion.openrefine.csv_file_dir";
	
	public static final String COLFUSION_OPENREFINE_CSV_FILE_NAME = "colfusion.openrefine.csv_file_name";
}
