package io.robe.common.exception;

import io.robe.common.dto.RobeMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * A customized {@link javax.ws.rs.WebApplicationException}.
 * It helps to code clean and easy.
 */
public class RobeRuntimeException extends WebApplicationException {

    private RobeMessage.Builder builder;

    public RobeRuntimeException(String message, Exception e) {
        super(message, e);
        String detail = message == null ? e.getMessage() : message;
        builder = new RobeMessage.Builder().message(detail);
    }

    public RobeRuntimeException(Exception e) {
        this(null, e);
    }

    public RobeRuntimeException(String message) {
        this(message, null);
    }

    /**
     * @param code
     * @return
     */
    public RobeRuntimeException code(String code) {
        this.builder.code(code);
        return this;
    }

    /**
     * @param status
     * @return
     */
    public RobeRuntimeException status(int status) {
        this.builder.status(status);
        return this;
    }

    /**
     * @param message
     * @return
     */
    public RobeRuntimeException message(String message) {
        this.builder.message(message);
        return this;
    }

    /**
     * @param moreInfo
     * @return
     */
    public RobeRuntimeException moreInfo(String moreInfo) {
        this.builder.moreInfo(moreInfo);
        return this;
    }

    /**
     * Gets current message
     *
     * @return current message
     */
    public String getMessage() {
        return builder.build().getMessage();
    }

    /**
     * @return {@link io.robe.common.dto.BasicPair#toString()}
     */
    @Override
    public String toString() {
        return builder.build().toString();
    }

    /**
     * Gets current response created with exception parameters.
     *
     * @return current response
     */
    public Response getResponse() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(builder.build()).build();
    }

    /**
     * Gets current response created with exception parameters.
     *
     * @param id
     * @return
     */
    public Response getResponse(String id) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(builder.id(id).build()).build();
    }
}
