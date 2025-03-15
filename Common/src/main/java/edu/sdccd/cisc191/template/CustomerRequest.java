package edu.sdccd.cisc191.template;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerRequest {
    private Map<String,Object> attributesToModify;
    private Integer id;
    private String requestType;

    //BEGIN MAKING CLASS SERIALIZABLE
    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String toJSON(CustomerRequest customer) throws Exception {
        return objectMapper.writeValueAsString(customer);
    }
    public static CustomerRequest fromJSON(String input) throws Exception{
        return objectMapper.readValue(input, CustomerRequest.class);
    }
    protected CustomerRequest() {}
    //END MAKING CLASS SERIALIZABLE

    public CustomerRequest(String requestType, Integer id) {
        this.requestType = requestType;
        this.id = id;
        this.attributesToModify = new HashMap<>();
    }

    // Constructor for modify requests
    public CustomerRequest(String requestType, int id, Map<String, Object> attributesToModify) {
        this.requestType = requestType;
        this.id = id;
        this.attributesToModify = attributesToModify;
    }

    // Getters and Setters
    public Map<String, Object> getAttributesToModify() {
        return attributesToModify;
    }

    @Override
    public String toString() {
        return String.format(

               """
                Request Type: [type=%s]
                RequestID: [id=%d]
                """, requestType, id);
    }

    public Integer getId() {
        return id;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}