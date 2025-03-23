package edu.sdccd.cisc191.template;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a customer request within the system. Each request includes
 * a request type, an associated ID, and optional attributes for modification.
 *
 * <p>This class supports serialization to and deserialization from JSON format
 * to enable easy data exchange.</p>
 *
 * @author Andy Ly, Andrew Huang
 */
public class CustomerRequest {

    private Map<String, Object> attributesToModify;
    private Integer id;
    private String requestType;

    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Serializes a <code>CustomerRequest</code> object into a JSON string.
     *
     * @param customer The <code>CustomerRequest</code> object to serialize.
     * @return A JSON string representation of the <code>CustomerRequest</code>.
     * @throws Exception If serialization fails.
     */
    public static String toJSON(CustomerRequest customer) throws Exception {
        return objectMapper.writeValueAsString(customer);
    }

    /**
     * Deserializes a JSON string into a <code>CustomerRequest</code> object.
     *
     * @param input The JSON string to deserialize.
     * @return A <code>CustomerRequest</code> object created from the JSON string.
     * @throws Exception If deserialization fails.
     */
    public static CustomerRequest fromJSON(String input) throws Exception {
        return objectMapper.readValue(input, CustomerRequest.class);
    }

    /**
     * Default constructor for <code>CustomerRequest</code>.
     * Required for JSON serialization/deserialization.
     */
    protected CustomerRequest() {
        // Default constructor for deserialization purposes
    }

    /**
     * Creates a <code>CustomerRequest</code> with a specified request type and ID.
     *
     * @param requestType The type of the request.
     * @param id The ID associated with the request.
     */
    public CustomerRequest(String requestType, Integer id) {
        this.requestType = requestType;
        this.id = id;
        this.attributesToModify = new HashMap<>();
    }

    /**
     * Creates a <code>CustomerRequest</code> with a specified request type, ID, and
     * attributes to modify.
     *
     * @param requestType The type of the request.
     * @param id The ID associated with the request.
     * @param attributesToModify A map of attributes to be modified.
     */
    public CustomerRequest(String requestType, int id, Map<String, Object> attributesToModify) {
        this.requestType = requestType;
        this.id = id;
        this.attributesToModify = attributesToModify;
    }

    /**
     * Retrieves the attributes to be modified for this request.
     *
     * @return A map containing the attributes to modify.
     */
    public Map<String, Object> getAttributesToModify() {
        return attributesToModify;
    }

    /**
     * Converts the <code>CustomerRequest</code> object into a string representation.
     *
     * @return A string containing the request type and ID.
     */
    @Override
    public String toString() {
        return String.format(
                """
                Request Type: [type=%s]
                RequestID: [id=%d]
                """, requestType, id);
    }

    /**
     * Retrieves the ID associated with this request.
     *
     * @return The request ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Retrieves the type of this request.
     *
     * @return The request type.
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the ID associated with this request.
     *
     * @param id The new request ID.
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
