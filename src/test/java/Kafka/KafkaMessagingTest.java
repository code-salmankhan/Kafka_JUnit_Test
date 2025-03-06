package Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.*;

public class KafkaMessagingTest {
    private static KafkaContainer kafkaContainer;
    private static String bootstrapServers;
    private static final String TOPIC = "test-topic";

    @BeforeAll
    public static void startKafka() {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        kafkaContainer.start();
        bootstrapServers = kafkaContainer.getBootstrapServers();
    }

    @AfterAll
    public static void stopKafka() {
        kafkaContainer.stop();
    }

    @Test
    public void testKafkaMessageFlow() {
        KafkaTestProducer producer = new KafkaTestProducer(bootstrapServers);
        KafkaTestConsumer consumer = new KafkaTestConsumer(bootstrapServers, TOPIC, "test-group");

        // Send a message
        String messageKey = "key1";
        String messageValue = "Hello Kafka!";
        producer.sendMessage(TOPIC, messageKey, messageValue);

        // Poll messages
        ConsumerRecords<String, String> records = consumer.pollMessages();
        assertFalse(records.isEmpty(), "No messages received");  // ✅ JUnit 5 assertion

        for (ConsumerRecord<String, String> record : records) {
            assertEquals(messageKey, record.key());
            assertEquals(messageValue, record.value());
        }

        // Cleanup
        producer.close();
        consumer.close();
    }
}