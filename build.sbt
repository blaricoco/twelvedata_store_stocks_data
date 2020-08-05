name := "twelvedata"

version := "0.1"

scalaVersion := "2.11.5"

libraryDependencies += "com.mashape.unirest" % "unirest-java" % "1.4.9"
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.3.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.5"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.4"
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.4"
libraryDependencies += "junit" % "junit" % "4.11" % Test
libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.3.0"
libraryDependencies += "org.apache.hbase" % "hbase-common" % "2.3.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "3.2.0"
libraryDependencies += "info.batey.kafka" % "kafka-unit" % "1.0"