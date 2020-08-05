package producer_test;

import info.batey.kafka.unit.KafkaUnit;
import info.batey.kafka.unit.KafkaUnitRule;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

public class Kafka_integration_test {

    @Rule
    public KafkaUnitRule kafkaUnitRule = new KafkaUnitRule(5000, 5001);


    @Test
    public void test_should_have_started_kafka() throws Exception {
        KafkaUnitRule kafkaUnitRule = new KafkaUnitRule();
        KafkaUnit kafkaUnitServer = new KafkaUnit();
        kafkaUnitServer.startup();

        String testTopic = "TestTopic";
        kafkaUnitRule.getKafkaUnit().createTopic(testTopic);
        ProducerRecord<String, String> keyedMessage = new ProducerRecord<>(testTopic, "key", "value");

        kafkaUnitServer.sendMessages(keyedMessage);
        List<String> messages = kafkaUnitRule.getKafkaUnit().readMessages(testTopic, 1);
        assertEquals(Arrays.asList("value"), messages);
        kafkaUnitServer.shutdown();
    }
}
