package com.migration.example.migrationscript.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migration.example.migrationscript.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KafkaConsumer {
    private final MongoTemplate mongoTemplate;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private static final String topicName = "dataForMongo3";
    private static final String groupIdName1 = "user-data-group-id-10";
    ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public KafkaConsumer(MongoTemplate mongoTemplate, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        this.mongoTemplate = mongoTemplate;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

    @KafkaListener(id = "user-data-listener-id-1", topics = topicName, groupId = groupIdName1, autoStartup = "false", concurrency = "4")
    public void consume1(@Payload List<String> message) {
        processMessage(message);
    }

    private void processMessage(List<String> messages) {
        List<UserData> dataList = messages.stream()
                                    .map(this::objectConversion)
                                    .filter(e -> e != null)
                                    .collect(Collectors.toList());
        List<UserData> outputList = mongoTemplate.insert(dataList, UserData.class).stream().toList();

        if (outputList.size() == dataList.size()) {
            System.out.println("Batch Processed Successfully");
        }

        // check which events have not processed successfully
    }

    private UserData objectConversion(String message) {
        try {
            return objectMapper.readValue(message, UserData.class);
        } catch (JsonProcessingException e) {
            System.out.println("Invalid Message received: " + message);
            return null;
        }
    }

    private void processMessage(String message) {
        try {
            // Deserialize JSON to UserData object
            UserData userData = objectMapper.readValue(message, UserData.class);

            System.out.println("Listener consumed from Kafka: " + userData.getUserId());

            // Store in MongoDB
            mongoTemplate.save(userData);

            System.out.println("Listener saved in MongoDB: " + userData.getUserId());
        } catch (JsonProcessingException e) {
            System.err.println("Listener error parsing JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Listener error consuming message: " + e.getMessage());
        }
    }

    public void startConsuming() {
        startListener("user-data-listener-id-1");
    }

    public void stopConsuming() {
        stopListener("user-data-listener-id-1");
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


