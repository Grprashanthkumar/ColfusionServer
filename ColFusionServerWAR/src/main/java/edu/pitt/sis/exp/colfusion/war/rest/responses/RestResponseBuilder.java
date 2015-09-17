package edu.pitt.sis.exp.colfusion.war.rest.responses;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class RestResponseBuilder {
	//extends ViewModel
	public static <T> Response build(final T viewModel, final boolean isSuccessful,
			final String userMessage, final Status statusCode) {
		final RestResponse<T> restResponse = new RestResponseImpl<T>(viewModel, isSuccessful, userMessage);

		return Response.status(statusCode).entity(restResponse.toJson()).build();
	}
	//extends ViewModel
	public static <T> Response build(final List<T> viewModel, final boolean isSuccessful,
			final String userMessage, final Status statusCode) {
		final RestResponse<List<T>> restResponse = new RestResponseImpl<List<T>>(viewModel, isSuccessful, userMessage);

		return Response.status(statusCode).entity(restResponse.toJson()).build();
	}
}
