package edu.pitt.sis.exp.colfusion.psc.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;

import com.sun.jersey.api.json.JSONConfiguration;

import edu.pitt.sis.exp.colfusion.psc.server.ColfusionPSCServer;
import edu.pitt.sis.exp.colfusion.utils.ResourceUtils;
import edu.pitt.sis.exp.colfusion.utils.StreamUtils;

public class Utils {
	
	private static final Logger logger = LogManager.getLogger(Utils.class.getName());
	
	/**
	 * This class is intended to use be used as static only.
	 */
	private Utils() {}
	
	private final static String CONFIG_FILE_NAME = "config.properties";
	private static Properties properties;
	
	static {
		loadProperties();
	}
	
	private static void loadProperties() {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			String resourceURL = ColfusionPSCServer.class.getClassLoader().getResource(CONFIG_FILE_NAME).toString();
			
			logger.info(String.format("Loading properties from '%s'", resourceURL));
			
			String streamContent = StreamUtils.toString(ResourceUtils.getResourceAsStream(ColfusionPSCServer.class, CONFIG_FILE_NAME));
			
			logger.info(String.format("Resource Stream content is '%s'", streamContent));
			
			input = ResourceUtils.getResourceAsStream(ColfusionPSCServer.class, CONFIG_FILE_NAME);
			
			prop.load(input);
	 
			properties = prop;
			
			logger.info(String.format("Loaded properties are: ", prop.toString()));
	 
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}
	
	public static String getProperty(final String propertyKey) {
		return properties.getProperty(propertyKey);
	}
	
	public static String getProperty(final String propertyKey, final String defaultValue) {
		return properties.getProperty(propertyKey, defaultValue);
	}
	
	public static String getBaseRestURL(final ServerType serverType) {
		String host = Utils.getHost(serverType);
		String port = Utils.getPort(serverType);
		
		return String.format("http://%s:%s/rest", host, port);
	}
	
	public static String getHost(final ServerType serverType) {
		switch (serverType) {
			case FETCHER:
				return Utils.getProperty("colfusion.tablefetcher.host");
				
			case JOINER:
				return Utils.getProperty("colfusion.tablejoiner.host");
				
			default:
				String message = String.format("Unsupported server type '%s'", serverType);
				logger.error(message);
				throw new IllegalArgumentException(message);
		}
	}

	public static String getPort(final ServerType serverType) {
		
		switch (serverType) {
			case FETCHER:
				return Utils.getProperty("colfusion.tablefetcher.port");
				
			case JOINER:
				return Utils.getProperty("colfusion.tablejoiner.port");
				
			default:
				String message = String.format("Unsupported server type '%s'", serverType);
				logger.error(message);
				throw new IllegalArgumentException(message);
		}
	}
	
	private static Client getClient() {
		ClientConfig clientConfit = new ClientConfig();
		clientConfit.property(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = ClientBuilder.newClient(clientConfit);
		return client;
	}
	
	public static Response doGet(final String restResource) {
		
		Client client = getClient();
		
		WebTarget target = client.target(restResource);
		   
		logger.info("About to send get request to: " + restResource);
		
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		
		return response;
	}
	
	public static Response doPost(final String restResource, final Object data) {
		
		Client client = getClient();
		
		WebTarget target = client.target(restResource);
		   
		logger.info("About to send post request to: " + restResource);
		
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
		
		return response;
	}
}
