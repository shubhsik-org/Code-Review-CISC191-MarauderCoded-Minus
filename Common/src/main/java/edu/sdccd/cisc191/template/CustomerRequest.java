package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerRequest {
    private Integer id;
    private String requestType;

    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String toJSON(CustomerRequest customer) throws Exception {
        return objectMapper.writeValueAsString(customer);
    }
    public static CustomerRequest fromJSON(String input) throws Exception{
        return objectMapper.readValue(input, CustomerRequest.class);
    }
    protected CustomerRequest() {}

    public CustomerRequest(String requestType, Integer id) {
        this.requestType = requestType;
        this.id = id;
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