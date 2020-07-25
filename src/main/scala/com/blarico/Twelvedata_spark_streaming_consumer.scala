package com.blarico

import java.util.Properties

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{StringType, StructType}


class Twelvedata_spark_streaming_consumer(p: Properties) extends Thread{

  // Spark configuration
  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("twelvedata_streaming")
    .getOrCreate()

  // extra functionality
  spark.sparkContext.setLogLevel("ERROR")
  import spark.implicits._

  // Streaming data from kafka
  val messages = spark
    .readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", p.getProperty("bootstrap.servers"))
    .option("subscribe", p.getProperty("topic"))
    .option("startingOffsets", "earliest")
    .load()
    .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").as[(String, String)]

  // Schema
  val schema_call = new StructType()
    .add("meta", StringType, true)
    .add("values", StringType, true)
    .add("status", StringType, true)

  val schema_data = new StructType()
    .add("datetime", StringType, true)
    .add("open", StringType, true)
    .add("close", StringType, true)
    .add("low", StringType, true)
    .add("high", StringType, true)

  override def run(): Unit = {


    val df2 = messages.withColumn("jsonData", from_json(col("value"), schema_call)).select("jsonData.*")
    val df3 = df2.map(_.getString(1))


    df2.writeStream
      .format("console")
      .outputMode("update")
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .start()
      .awaitTermination()

  }
}
