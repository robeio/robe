package io.robe.exception;

import io.robe.dto.BasicPair;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class RobeRuntimeException extends WebApplicationException {

	private Response response;

	private BasicPair entity;

	public RobeRuntimeException (Exception e){
		this("Exception", e.getMessage());
		this.initCause(e);
	}
	public RobeRuntimeException (String name,Exception e){
		this(name,e.getMessage());
		this.initCause(e);
	}
	public RobeRuntimeException(String name, String message) {
		entity = new BasicPair(name,message);
		response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(entity).build();
	}

	public String getName() {
		return entity.getName();
	}

	public String getMessage() {
		return entity.getValue();
	}

	@Override
	public String toString() {
		return entity.toString();
	}

	public Response getResponse() {
		return response;
	}
}
