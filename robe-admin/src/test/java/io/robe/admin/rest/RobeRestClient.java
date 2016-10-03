package io.robe.admin.rest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.robe.admin.rest.http.HttpRequest;
import io.robe.admin.rest.http.HttpRequestImpl;
import okhttp3.Request;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @desciption Robe Rest Client use for Crud Operations more easy and other can use for custom request.
 * @param <E>
 * @param <I>
 */
public class RobeRestClient<E, I> {
    /**
     * Use for Json from Java and Java from Json conversions
     */
    public static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * holds resource main url
     */
    private String baseUrl;
    /**
     * holds resource entity type
     */
    private final Class<E> type;
    /**
     *  holds resource list of entity type.
     */
    private final JavaType listType;


    private HttpRequest httpRequest;

    /**
     *
     * @param type
     * @param url
     */
    public RobeRestClient(Class<E> type, String url){
        this(new HttpRequestImpl(), type, url);
    }
    /**
     *
     * @param type
     * @param url
     */
    public RobeRestClient(HttpRequest httpRequest, Class<E> type, String url){
        listType = MAPPER.getTypeFactory().
                constructCollectionType(List.class, type);
        this.type = type;
        this.baseUrl = "http://127.0.0.1:8080/robe/" + url;
        this.httpRequest = httpRequest;
    }

    /**
     *
     * @param value
     * @return
     */
    public List<E> stringToModelList(String value){
        try {
            return MAPPER.readValue(value, listType);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param value
     * @return
     */
    public E stringToModel(String value){
        try {
            return MAPPER.readValue(value, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param type
     * @return
     */
    public String typeToString(Object type) {
        try {
            return MAPPER.writeValueAsString(type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param result
     * @return
     */
    protected Response<E> resultToResponseModel(Response<String> result){
        Response<E> response = new Response<>();
        response.setStatus(result.getStatus());
        if(result.getData() != null) {
            response.setData(stringToModel(result.getData()));
        }
        response.setHeaderMap(result.getHeaderMap());
        return response;
    }


    /**
     *
     * @param result
     * @return
     */
    protected <T> Response<T> resultToResponseModel(Response<String> result, Class<T> type) throws IOException {
        Response<T> response = new Response<>();
        response.setStatus(result.getStatus());
        if(response.getData() != null) {
            T element = MAPPER.readValue(result.getData(), type);
            response.setData(element);
        }
        response.setHeaderMap(result.getHeaderMap());
        return response;
    }

    /**
     *
     * @param result
     * @return
     */
    protected Response<List<E>> resultToResponseList(Response<String> result){
        Response<List<E>> response = new Response<>();
        response.setStatus(result.getStatus());
        if(result.getData() != null) {
            response.setData(stringToModelList(result.getData()));
        }
        response.setHeaderMap(result.getHeaderMap());
        return response;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public Response<List<E>> getAll() throws Exception {
        return resultToResponseList(httpRequest.get(this.baseUrl, null, "", null));
    }
    /**
     *
     * @param queryMap
     * @return
     * @throws Exception
     */
    public Response<List<E>> getAll(Map<String, String> queryMap) throws Exception {
        return resultToResponseList(httpRequest.get(this.baseUrl, null, queryMap, null));
    }
    /**
     *
     * @param queryString
     * @return
     * @throws Exception
     */
    public Response<List<E>> getAll(String queryString) throws Exception {
        return resultToResponseList(httpRequest.get(this.baseUrl, null, queryString, null));
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Response<E> get(String id) throws Exception {
        return resultToResponseModel(httpRequest.get(this.baseUrl, id, "", null));
    }

    public Response<E>  create(E model) throws Exception {
        return resultToResponseModel(httpRequest.post(this.baseUrl, typeToString(model)));
    }

    public Response<E>  update(String id, E model) throws Exception {
        return resultToResponseModel(httpRequest.put(this.baseUrl, id , typeToString(model)));
    }

    public Response<E>  merge(String id, E model) throws Exception {
        return resultToResponseModel(httpRequest.patch(this.baseUrl, id , typeToString(model)));
    }

    public Response<E> delete(String id, E model) throws Exception {
        return resultToResponseModel(httpRequest.delete(this.baseUrl, id , typeToString(model)));
    }

    public <T> Response<T> request(Request.Builder builder, Class<T> typeClass) throws Exception {
        return resultToResponseModel(httpRequest.request(builder),typeClass);
    }
}
