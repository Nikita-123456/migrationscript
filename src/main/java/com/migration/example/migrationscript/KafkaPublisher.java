//package com.migration.example.migrationscript;
//
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaPublisher {
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void publishToKafka(String topic, String message) {
//        kafkaTemplate.send(topic, message);
//        System.out.println("sent on kafka "+ topic);
//    }
//}

package com.migration.example.migrationscript;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.lang.Nullable;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, String>> send(String topic, String userId, @Nullable String message) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, userId, message);
        return kafkaTemplate.send(producerRecord);
    }

    public void publishToKafka(String topic, String userId, String message) {
        this.send(topic, userId, message).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
