package com.blarico

import java.util.Properties

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{StringType, StructType}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}


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
    .load()
    .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").as[(String, String)]

  // Schema
  val schema_call = new StructType()
    .add("meta", StringType, true)
    .add("values", StringType, true)
    .add("status", StringType, true)

  // Rearrange data from dataframe
  def transform_data(dataframe: DataFrame): DataFrame={

    val rdd = dataframe.rdd.map(_.getString(1))
    val ds = rdd.toDS()
    val df = spark.read.json(ds)
    df.show(30)
    df
  }

  // Check for values
  def record_check(record: Row): Row={

    println(record, 1)
    record
  }

  override def run(): Unit = {

    var sum = 0.0

    val df = messages.withColumn("jsonData", from_json(col("value"), schema_call)).select("jsonData.*")

    df.writeStream
      .format("console")
      .outputMode("append")
      .foreachBatch { (batchDF: DataFrame, batchId: Long) =>
        val data = transform_data(batchDF)
        Thread.sleep(1000)
        val data2 = data.withColumn("difference",col("high") - col("low"))
        val mean = data2.select("difference").rdd.map(_(0).asInstanceOf[Double]).reduce(_+_) / 30
        println(mean)

      }
      .start()
      .awaitTermination()

  }
}
