package com.blarico

import java.util.Properties

import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.client.{ColumnFamilyDescriptorBuilder, ConnectionFactory, Put, Row, TableDescriptorBuilder}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Row

class Twelvedata_HBase_functionality(p: Properties) extends  Serializable {

  // HBase configuration
  val tableName = TableName.valueOf(p.getProperty("table_name"))
  val conf = HBaseConfiguration.create()
  conf.addResource(new Path(p.getProperty("hbase_config")))
  val connection = ConnectionFactory.createConnection(conf)
  val admin = connection.getAdmin

  def buildPut(record: String) ={

    // Split rows into sections
    val data = record.split(",")
    val key = Bytes.toBytes(data(1))
    val row = new Put(key)

    // Map data for each field using date as the key
    val cf = Bytes.toBytes(p.getProperty("cf"))
    row.addColumn(cf, Bytes.toBytes("open"), Bytes.toBytes(data(4)))
    row.addColumn(cf, Bytes.toBytes("high"), Bytes.toBytes(data(2)))
    row.addColumn(cf, Bytes.toBytes("low"), Bytes.toBytes(data(3)))
    row.addColumn(cf, Bytes.toBytes("close"), Bytes.toBytes(data(0)))
    row
  }

  def save_dataframe(data: Array[org.apache.spark.sql.Row]): Unit ={

    // Iterate over each record to save save into hbase
    val table = connection.getTable(tableName)
    data.foreach(record => {
      table.put(buildPut(record.toString()))
    })
    table.close()
    connection.close()
  }

  def create_table(): Unit = {

    // Create column family specified in application.properties
    val cf1 = ColumnFamilyDescriptorBuilder.newBuilder(p.getProperty("cf").getBytes())

    // Create table if it does not exist
    if (!admin.tableExists(tableName)) {
      val tableDesc = TableDescriptorBuilder.newBuilder(tableName)
      tableDesc.setColumnFamily(cf1.build())

      admin.createTable(tableDesc.build())
      println("Created!!!!")
    }
    else {
      println("Already exists!")
    }
  }
}
