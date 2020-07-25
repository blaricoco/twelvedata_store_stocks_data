package producer_test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.blarico.Twelvedata_API_call;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class API_call_test {

    // Load application properties
    Properties p = new Properties();
    InputStream is;
    {
        try {
            is = new FileInputStream("application.properties");
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_toCheckTheOutputof_apiCall_isString() {
        Twelvedata_API_call apiCall_test = new Twelvedata_API_call();
        String expected = "";
        String output = apiCall_test.getStocks(p);
        assertEquals(expected.getClass(), output.getClass());
    }

    @Test
    public void test_toCheckThat_whenTheAPI_IsCalled_TheReturnIsNot_Empty() {
        Twelvedata_API_call apiCall_test = new Twelvedata_API_call();
        String output = apiCall_test.getStocks(p);
        assertFalse(output.isEmpty());
    }

    @Test
    public void test_To_checkTheMetadataOf_APICall_HasTheSpecified_Parameters_expectedProperties() {

        Twelvedata_API_call apiCall_test = new Twelvedata_API_call();
        String output = apiCall_test.getStocks(p);

        JsonObject expected = new JsonObject();
        expected.addProperty("symbol","EUR/USD");
        expected.addProperty("interval","1min");
        expected.addProperty("currency_base","Euro");
        expected.addProperty("currency_quote","US Dollar");
        expected.addProperty("type","Physical Currency");

        JsonObject test_json = new Gson().fromJson(output, JsonObject.class);

        assertEquals(expected, test_json.get("meta"));
    }

    @Test
    public void test_To_checkTheMetadataOf_APICall_HasTheSpecified_Parameters_expectedStatus_ok() {

        Twelvedata_API_call apiCall_test = new Twelvedata_API_call();
        String output = apiCall_test.getStocks(p);

        JsonObject expected = new JsonObject();
        expected.addProperty("status","ok");


        JsonObject test_json = new Gson().fromJson(output, JsonObject.class);

        assertEquals(expected.get("status"), test_json.get("status"));
    }


    @Test
    public void test_To_checkTheMetadataOf_APICall_HasTheSpecified_Parameters_expectedKeys_meta_values_status() {

        int result = 0;
        Twelvedata_API_call apiCall_test = new Twelvedata_API_call();
        String output = apiCall_test.getStocks(p);

        JsonObject test_json = new Gson().fromJson(output, JsonObject.class);
        for(JsonElement record : test_json.get("values").getAsJsonArray()) {
            System.out.println(record);
            result++;
        }
        assertEquals(30, result);
    }
}
