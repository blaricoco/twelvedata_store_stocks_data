package com.blarico

import java.io.{FileInputStream, FileOutputStream, OutputStream}
import java.util.Properties

object Twelvedata_application {
  def main(args: Array[String]): Unit = {
    // Load properties from application.properties
    val p = new Properties()
    p.load(new FileInputStream("application.properties"))

    val producer_thread = new Twelvedata_producer(p)
    val consumer_spark_batch_thread = new Twelvedata_spark_batch_consumer(p)
    val consumer_spark_streaming_thread = new Twelvedata_spark_streaming_consumer(p)

    producer_thread.start()
    //consumer_spark_batch_thread.start()
    consumer_spark_streaming_thread.start()
  }
}
