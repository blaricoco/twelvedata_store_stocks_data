package com.blarico

import java.util.Properties

import org.apache.spark.rdd
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructType}
import org.apache.spark.sql.functions.{col, from_json}

class Twelvedata_spark_batch_consumer(p: Properties) extends Thread {

  // Spark configuration
  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("twelvedata_batch")
    .getOrCreate()

  // extra functionality
  spark.sparkContext.setLogLevel("ERROR")
  import spark.implicits._

  // Reading topic from kafka
  val lines = spark
    .read
    .format("kafka")
    .option("kafka.bootstrap.servers", p.getProperty("bootstrap.servers"))
    .option("subscribe", p.getProperty("topic"))
    .option("maxOffsetsPerTrigger", "1")
    .load()

  // Schema
  val schema_call = new StructType()
    .add("meta", StringType, true)
    .add("values", StringType, true)
    .add("status", StringType, true)

  // HBase functionality
  val hbase = new Twelvedata_HBase_functionality(p)

  override def run(): Unit = {

    var running = true

    while(running){

      val df = lines.selectExpr("CAST(value AS STRING)")
      df.printSchema()
      df.show(false)

      val df2 = df.withColumn("jsonData", from_json(col("value"), schema_call)).select("jsonData.*")
      df2.printSchema()
      df2.show(false)

      val rdd = df2.rdd.map(_.getString(1))
      val ds = rdd.toDS()
      val df3 = spark.read.json(ds)
      df3.printSchema()
      df3.show(false)

      // Create Hbase table
      hbase.create_table()

      // Save to hbase
      val data = df3.collect()
      hbase.save_dataframe(data)

      running = false
    }

  }
}
