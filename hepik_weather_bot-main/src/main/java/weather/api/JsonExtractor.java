package main.java.weather.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonExtractor<T> {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Parses and generates a tree from json string passed in.
     *
     * @param json denotes a json string
     * @return a {@link JsonNode} that contains the parsed json tree.
     * @throws JsonProcessingException if an error is encountered while parsing the json string.
     */
    public JsonNode readJsonTree(String json) throws JsonProcessingException {
        return OBJECT_MAPPER.readTree(json);
    }

    /**
     * Extracts a json object into a POJO (Plain Old Java Object) class. This is method is great when mapping
     * to and from an object whose fields won't cause any issue.
     *
     * @param jsonNode denotes the parsed json node.
     * @param tClass denotes the Java Class type of the object that will hold the Java object
     * @return the POJO (Plain Old Java Object) object with the extracted data
     * @throws JsonProcessingException when error is encountered while extracting the json
     */
    public T extractFromJson(JsonNode jsonNode, Class<T> tClass) throws JsonProcessingException {
        // Map to object directly
        return OBJECT_MAPPER.convertValue(jsonNode, tClass);
    }

    /**
     * Serializes any Java to its rudimentary json string representation.
     *
     * @param object denotes an object to encode.
     * @return a json string
     * @throws JsonProcessingException if an error is encountered while serializing the object to a json string
     */
    public String convertToJson(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    /**
     * Extracts a json object into a POJO (Plain Old Java Object) class. This method explicitly requires a specific key
     * to be present for the json to be mapped to an object.
     *
     * @param json denotes the json string.
     * @param anchorKey denotes a key that must be present for the json to be mapped to an object.
     * @param tClass denotes the Java Class type of the object that will hold the Java object
     * @return the POJO (Plain Old Java Object) object with the extracted data
     * @throws JsonProcessingException when error is encountered while extracting the json
     */
    public T extractFromJson(String anchorKey, String json, Class<T> tClass) throws JsonProcessingException {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(json);

        // If absent, no need to map
        if (!jsonNode.has(anchorKey)) return null;

        return OBJECT_MAPPER.convertValue(jsonNode, tClass);
    }

    /**
     * Reads any nested json objects within another json string
     *
     * @param json denotes the json string to parse and iterate
     * @param keys denotes the key path to the desired object. Must be provided in order of occurrence in a json object
     * @return the nested object as a String
     * @throws JsonProcessingException if an error is encountered while reading a node in the parsed json object
     */
    public String readNestedJson(String json, String[] keys) throws JsonProcessingException {
        if (keys.length > 0) {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(json); // Parse json

            // Loop all keys and read json nodes
            for (String key: keys)
                jsonNode = jsonNode.get(key);

            return jsonNode.toPrettyString(); // Read text
        }

        return json;
    }
}
