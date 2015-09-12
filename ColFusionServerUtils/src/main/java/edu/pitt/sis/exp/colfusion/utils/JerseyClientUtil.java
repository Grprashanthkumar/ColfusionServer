package edu.pitt.sis.exp.colfusion.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

public class JerseyClientUtil {

	final static Logger logger = LogManager.getLogger(JerseyClientUtil.class.getName());

	private static Client getClient() {

		final ClientConfig clientConfit = new ClientConfig();
		clientConfit.register(JacksonFeature.class);
		final Client client = ClientBuilder.newClient(clientConfit);
		return client;
	}

	/**
	 * Will send get request to the address specified by the parameter.
	 *
	 * @param restContext the context of the rest (e.g. the base part of the REST API URL)
	 * @param restResource the resource name with params. MAKE SURE it starts with "/"
	 * @return
	 */
	public static Response doGet(final String restContext,  final String restResource) {

		logger.info(String.format("About to do Get ruquest to %s%s", restContext, restResource));

		final WebTarget target = getTarget(restContext, restResource);

		final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

		logger.info(String.format("Got reply from Get %s%s. Response status is %d", restContext, restResource, response.getStatus()));

		return response;
	}

	/**
	 * Will send post request to the address specified by the parameter.
	 *
	 * @param restContext the context of the rest (e.g. the base part of the REST API URL)
	 * @param restResource the resource name with params. MAKE SURE it starts with "/"
	 * @param data the data to send (your view model)
	 * @return
	 */
	public static Response doPost(final String restContext,  final String restResource, final Object data) {

		logger.info(String.format("About to do Post ruquest to %s%s", restContext, restResource));

		final WebTarget target = getTarget(restContext, restResource);

		final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));

		logger.info(String.format("Got reply from Post %s%s. Response status is %d", restContext, restResource, response.getStatus()));

		return response;
	}

	/**
	 * Will send post request to the address specified by the parameter.
	 *
	 * @param restContext the context of the rest (e.g. the base part of the REST API URL)
	 * @param restResource the resource name with params. MAKE SURE it starts with "/"
	 * @param data the data to send (your view model)
	 * @return
	 */
	public static Response doPut(final String restContext,  final String restResource, final Object data) {

		logger.info(String.format("About to do Put ruquest to %s%s", restContext, restResource));

		final WebTarget target = getTarget(restContext, restResource);

		final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));

		logger.info(String.format("Got reply from Put %s%s. Response status is %d", restContext, restResource, response.getStatus()));

		return response;
	}

	private static WebTarget getTarget(final String restContext,
			final String restResource) {
		final Client client = getClient();

		final String url = restContext + restResource;

		final WebTarget target = client.target(url);
		return target;
	}
}