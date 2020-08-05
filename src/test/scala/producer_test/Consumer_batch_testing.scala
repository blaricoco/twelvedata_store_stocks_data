package producer_test

import java.io.FileInputStream
import java.util.Properties

import com.blarico.Twelvedata_spark_batch_consumer
import org.junit.Test
import org.junit.Assert._

class Consumer_batch_testing {


  // Load configuration in application.properties
  val p = new Properties()
  val in = new FileInputStream("application.properties")
  p.load(in)

  // Create instance of consumer
  val consumer = new Twelvedata_spark_batch_consumer(p)

  @Test def test_get_data_functionality_expecting_array_of_values_from_kafka_topic: Unit ={
    val test = consumer.get_data()
    assertNotNull(test)
  }

  @Test def test_data_from_call_expecting_topic_name_twelveData: Unit ={
    val expected = "twelveData"
    val actual = consumer.get_data().select("topic").collect()
    actual.foreach(x => println(x))

    assertEquals(expected, actual)
  }

  @Test def test_data_from_call_expecting_data: Unit ={
    val test = consumer.get_data().select("topic")
    test.foreach(x => println(x))
    assertEquals("", test)
  }
}
