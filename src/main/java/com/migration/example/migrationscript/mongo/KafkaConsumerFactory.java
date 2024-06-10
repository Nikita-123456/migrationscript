package com.migration.example.migrationscript.mongo;


import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerFactory {

    private int maxPollRecords = 1000; // Default value

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }
}
