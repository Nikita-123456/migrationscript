
//package com.migration.example.migrationscript;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.listener.KafkaMessageListenerContainer;
//import org.springframework.kafka.listener.MessageListenerContainer;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
//import org.springframework.kafka.listener.MessageListenerContainer;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaConsumer {
//    private final MongoTemplate mongoTemplate;
//    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
//
//    private static final String topicName = "dataForMongo3";
//    private static final String groupIdName = "user-data-group-id-9";
//
//    @Autowired
//    public KafkaConsumer(MongoTemplate mongoTemplate, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
//        this.mongoTemplate = mongoTemplate;
//        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
//    }
//
//
//    @KafkaListener(id = "user-data-listener-id", topics = topicName, groupId = groupIdName, autoStartup = "false")
//    public void consume(String message) {
//        try {
//            // Deserialize JSON to UserData object
//            ObjectMapper objectMapper = new ObjectMapper();
//            UserData userData = objectMapper.readValue(message, UserData.class);
//
//            System.out.println("Consumed from Kafka"+userData.getUserId());
//
//            // Store in MongoDB
//            mongoTemplate.save(userData);
//
//            System.out.println("Saved in MongoDB"+userData.getUserId());
//        } catch (JsonProcessingException e) {
//            System.err.println("Error parsing JSON: " + e.getMessage());
//        } catch (Exception e) {
//            System.err.println("Error consuming message: " + e.getMessage());
//        }
//    }
//
//    public void startConsuming() {
//        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("user-data-listener-id");
//        if (listenerContainer != null && !listenerContainer.isRunning()) {
//            listenerContainer.start();
//            System.out.println("Kafka listener started.");
//        } else {
//            System.out.println("Kafka listener is already running.");
//        }
//    }
//
//    public void stopConsuming() {
//        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("user-data-listener-id");
//        if (listenerContainer != null && listenerContainer.isRunning()) {
//            listenerContainer.stop();
//            System.out.println("Kafka listener stopped.");
//        } else {
//            System.out.println("Kafka listener is not running.");
//        }
//    }
//}



package com.migration.example.migrationscript;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private final MongoTemplate mongoTemplate;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private static final String topicName = "dataForMongo3";
    private static final String groupIdName1 = "user-data-group-id-10";
    private static final String groupIdName2 = "user-data-group-id-11"; // New consumer group ID
    private static final String groupIdName3 = "user-data-group-id-12"; // Another new consumer group ID
    private static final String groupIdName4 = "user-data-group-id-13"; // Another new consumer group ID

    @Autowired
    public KafkaConsumer(MongoTemplate mongoTemplate, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        this.mongoTemplate = mongoTemplate;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

    @KafkaListener(id = "user-data-listener-id-1", topics = topicName, groupId = groupIdName1, autoStartup = "false")
    public void consume1(String message) {
        processMessage(message, 1);
    }

    @KafkaListener(id = "user-data-listener-id-2", topics = topicName, groupId = groupIdName2, autoStartup = "false")
    public void consume2(String message) {
        processMessage(message, 2);
    }

    @KafkaListener(id = "user-data-listener-id-3", topics = topicName, groupId = groupIdName3, autoStartup = "false")
    public void consume3(String message) {
        processMessage(message, 3);
    }

    @KafkaListener(id = "user-data-listener-id-4", topics = topicName, groupId = groupIdName4, autoStartup = "false")
    public void consume4(String message) {
        processMessage(message, 4);
    }

    private void processMessage(String message, int listenerId) {
        try {
            // Deserialize JSON to UserData object
            ObjectMapper objectMapper = new ObjectMapper();
            UserData userData = objectMapper.readValue(message, UserData.class);

            System.out.println("Listener " + listenerId + " consumed from Kafka: " + userData.getUserId());

            // Store in MongoDB
            mongoTemplate.save(userData);

            System.out.println("Listener " + listenerId + " saved in MongoDB: " + userData.getUserId());
        } catch (JsonProcessingException e) {
            System.err.println("Listener " + listenerId + " error parsing JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Listener " + listenerId + " error consuming message: " + e.getMessage());
        }
    }

    public void startConsuming() {
        startListener("user-data-listener-id-1");
        startListener("user-data-listener-id-2");
        startListener("user-data-listener-id-3");
        startListener("user-data-listener-id-4");
    }

    public void stopConsuming() {
        stopListener("user-data-listener-id-1");
        stopListener("user-data-listener-id-2");
        stopListener("user-data-listener-id-3");
        stopListener("user-data-listener-id-4");
    }

    private void startListener(String listenerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(listenerId);
        if (listenerContainer != null && !listenerContainer.isRunning()) {
            listenerContainer.start();
            System.out.println("Kafka listener " + listenerId + " started.");
        } else {
            System.out.println("Kafka listener " + listenerId + " is already running.");
        }
    }

    private void stopListener(String listenerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(listenerId);
        if (listenerContainer != null && listenerContainer.isRunning()) {
            listenerContainer.stop();
            System.out.println("Kafka listener " + listenerId + " stopped.");
        } else {
            System.out.println("Kafka listener " + listenerId + " is not running.");
        }
    }
}


