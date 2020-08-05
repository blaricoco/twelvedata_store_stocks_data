package producer_test;

import com.blarico.Twelvedata_HBase_functionality;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.*;

public class HBase_functionality_test {

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
    public void test_HBase_functionality_creating_table_expecting_true_when_checking_table_exist() throws IOException {

        // functionality of creating a table
        Twelvedata_HBase_functionality hbase = new Twelvedata_HBase_functionality(p);
        hbase.create_table();

        // Check if set table exists
        TableName tableName = TableName.valueOf(p.getProperty("table_name"));
        Boolean exists = hbase.admin().tableExists(tableName);

        assertTrue(exists);
    }

    @Test
    public void test_HBase_functionality_build_put_expecting_to_create_one_family() throws IOException {

        // Test data
        String test_data = "test1,test2,test3,test4,test5";

        // functionality of creating a table
        Twelvedata_HBase_functionality hbase = new Twelvedata_HBase_functionality(p);
        Put actual = hbase.buildPut(test_data);

        // Check if set table exists
        int expected = 1;

        assertEquals(expected, actual.numFamilies());
    }

    @Test
    public void test_HBase_functionality_build_put_expecting_to_get_test2_from_test_data() throws IOException {

        // Test data
        // In my program the second element is the time stamp which I use for key
        String test_data = "test1,test2,test3,test4,test5";

        // functionality of creating a new put
        Twelvedata_HBase_functionality hbase = new Twelvedata_HBase_functionality(p);
        Put actual = hbase.buildPut(test_data);

        // Expected row string
        String expected = "test2";

        assertEquals(expected, new String(actual.getRow()));
    }

    @Test
    public void test_HBase_functionality_build_put_expecting_to_get_test1_from_test_data() throws IOException {

        // Test data
        String test_data = "test1,test2,test3,test4,test5";

        // functionality of creating a new put
        Twelvedata_HBase_functionality hbase = new Twelvedata_HBase_functionality(p);
        Put actual = hbase.buildPut(test_data);

        // Expected row string
        String expected = "test2";

        assertEquals(expected, actual.has(Bytes.toBytes("EURUSD"), Bytes.toBytes("test2")));
    }
}
