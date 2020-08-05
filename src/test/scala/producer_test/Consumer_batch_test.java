package producer_test;

import com.blarico.Twelvedata_spark_batch_consumer;
import org.apache.spark.sql.internal.SessionState;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Consumer_batch_test{



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

    // Instance new batch consumer
    Twelvedata_spark_batch_consumer consumer = new Twelvedata_spark_batch_consumer(p);

    @Test
    public void test_spark_connection_has_been_successfully_created_expecting_value_not_null() {
        consumer.run();
        SessionState actual = consumer.spark().sessionState();
        assertNotNull(actual);
    }

    @Test
    public void test_spark_master_expecting_to_run_on_local() {
        consumer.run();
        String expected = "local[*]";
        String actual = consumer.spark().conf().get("spark.master");
        assertEquals(actual,expected);
    }

    @Test
    public void test_spark_driver_host_expecting_localhost_192_168_1_165() {
        consumer.run();
        String expected = "192.168.1.165";
        String actual = consumer.spark().conf().get("spark.driver.host");
        assertEquals(actual,expected);
    }

    @Test
    public void test_spark_appName_expecting_twelvedata_batch() {
        consumer.run();
        String expected = "twelvedata_batch";
        String actual = consumer.spark().conf().get("spark.app.name");
        assertEquals(actual,expected);
    }
}
