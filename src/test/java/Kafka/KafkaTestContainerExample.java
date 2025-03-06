package Kafka;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaTestContainerExample {

    private static KafkaContainer kafkaContainer;

    @BeforeAll
    public static void setUp() {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        kafkaContainer.start();
        System.out.println("Kafka started at: " + kafkaContainer.getBootstrapServers());
    }

    @AfterAll
    public static void tearDown() {
        kafkaContainer.stop();
    }

    @Test
    public void testKafkaContainerRunning() {
        assert kafkaContainer.isRunning();
    }
}
