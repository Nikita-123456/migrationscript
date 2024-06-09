package com.migration.example.migrationscript.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migration.example.migrationscript.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class KafkaConsumer {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private static final String topicName = "dataForMongo3";
    private static final String groupIdName1 = "user-data-group-id-11";
    ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(id = "aashish_sql_migration", topics = "${spring.kafka.consumer.topic}", concurrency = "2", autoStartup = "false")
    public void consume(@Payload List<String> message) {
        processMessage(message);
    }

    private void processMessage(List<String> messages) {
        long startTime = System.currentTimeMillis();
        List<UserData> dataList = messages.stream()
                                    .map(this::objectConversion)
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList());
        List<UserData> outputList = mongoTemplate.insert(dataList, UserData.class).stream().toList();

        if (outputList.size() == dataList.size()) {
            System.out.println("Batch Processed Successfully." );

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("execution took (in kafka event consumption) " + duration);
        } else {
            System.out.println("Something went wrong");


            Set<UserData> successfulData = new HashSet<>(outputList);
            for (UserData userData : dataList) {
                if (!successfulData.contains(userData)) {
                    System.out.println("Failed to process: " + userData);
                }
            }
        }
        System.out.println(LocalDateTime.now());
        // TODO check which events have not processed successfully
    }

    private UserData objectConversion(String message) {
        try {
            return objectMapper.readValue(message, UserData.class);
        } catch (JsonProcessingException e) {
            System.out.println("Invalid Message received: " + message);
            return null;
        }
    }

    public void startConsuming() {
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(MessageListenerContainer::start);
    }

    public void stopConsuming() {
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(MessageListenerContainer::stop);
    }
}


