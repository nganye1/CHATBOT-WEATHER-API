package tests.java.phpBridge;

import main.java.phpBridge.PhpBridgeUtil;
import main.java.phpBridge.PhpRequest;
import main.java.phpBridge.PhpRequestTarget;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhpBridgeUtilTest {
    @Test
    @DisplayName("Extracts a location request & its input")
    public void testUtilityExtractsLocationInputs() {
        String input = "{\"request\": \"location\", \"input\": \"test\"}";

        try {
            PhpRequest request = PhpBridgeUtil.extractPhpRequest(input);

            assertEquals(PhpRequestTarget.REQUEST_LOCATION, request.target());
            assertEquals("\"test\"", request.input());
        } catch (Exception ignore) {
            fail("Should not have thrown an exception");
        }
    }

    @Test
    @DisplayName("Extracts a weather request & its input")
    public void testUtilityExtractsWeatherInputs() {
        String input = "{\"request\": \"weather\", \"input\": \"test\"}";

        try {
            PhpRequest request = PhpBridgeUtil.extractPhpRequest(input);

            assertEquals(PhpRequestTarget.REQUEST_WEATHER, request.target());
            assertEquals("\"test\"", request.input());
        } catch (Exception ignore) {
            fail("Should not have thrown an exception");
        }
    }

    @Test
    @DisplayName("Extracts an unknown request & its input")
    public void testUtilityExtractsUnknownInputs() {
        String input = "{\"request\": \"coordinates\", \"input\": \"test\"}";

        try {
            PhpRequest request = PhpBridgeUtil.extractPhpRequest(input);

            assertEquals(PhpRequestTarget.UNKNOWN, request.target());
            assertEquals("\"test\"", request.input());
        } catch (Exception ignore) {
            fail("Should not have thrown an exception");
        }
    }
}
