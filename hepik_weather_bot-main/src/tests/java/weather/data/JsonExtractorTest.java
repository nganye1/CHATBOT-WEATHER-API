package tests.java.weather.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import main.java.weather.api.JsonExtractor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonExtractorTest {
    // A custom class to use for testing
    private record TestUser(String name, int age) {}

    @Test
    public void testExtractFromJson() {
        String jsonSample = "{\"name\": \"HEPIK\", \"age\": 10}";

        JsonExtractor<TestUser> extractor = new JsonExtractor<>();

        try {
            assertEquals(new TestUser("HEPIK", 10), extractor.extractFromJson("name", jsonSample, TestUser.class));
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void testReadNestedJson() {
        String nestedJsonSample = "{\"root\" : {\"nested\":\"value\"}}";

        String output = """
                {
                  "nested" : "value"
                }""";

        JsonExtractor<String> extractor = new JsonExtractor<>();

        try {
            assertEquals(output, extractor.readNestedJson(nestedJsonSample, new String[]{"root"}));
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
    }
}
