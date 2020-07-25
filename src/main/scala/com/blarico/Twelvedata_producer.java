package com.blarico;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Twelvedata_producer extends Thread {

    private final KafkaProducer<Integer, String> producer;
    private final String topic;
    private final Properties p;

    public Twelvedata_producer(Properties p) {
        this.topic = p.getProperty("topic");
        this.p = p;
        Properties properties = new Properties();
        properties.put("bootstrap.servers", p.getProperty("bootstrap.servers"));
        properties.put("client.id", "twelvedata_producer");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<Integer, String>(properties);
    }

    public void run() {
        int messageNo = 1;

        while (true & messageNo < 5) {
            Twelvedata_API_call twelvedataAPI = new Twelvedata_API_call();
            String messageStr = "Message_" + messageNo;

            try {
                producer.send(new ProducerRecord<Integer, String>(
                        topic,
                        messageNo,
                        twelvedataAPI.getStocks(p)
                )).get();
                System.out.println("Sent message: (" + messageNo + ", " + messageStr + ")");
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            ++messageNo;
        }
    }
}