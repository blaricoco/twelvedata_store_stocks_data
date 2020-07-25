package producer_test;

import static org.junit.Assert.*;

import com.blarico.Twelvedata_producer;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Producer_test {

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
    public void test_to_check_the_statusOfThread_of_producer_to_be_alive_when_running() {
        Twelvedata_producer producerTest = new Twelvedata_producer(p);

        producerTest.start();
        Boolean output =  producerTest.isAlive();
        assertEquals( true, output);
    }

    @Test
    public void test_when_threadStart_expectedToHaveAnId_whenThreadItsCreated() {
        Twelvedata_producer producerTest = new Twelvedata_producer(p);

        Long expected = (long) 17;
        producerTest.start();
        Long output =  producerTest.getId();
        assertEquals(expected.getClass(), output.getClass());
    }
}
