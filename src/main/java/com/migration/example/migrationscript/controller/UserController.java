package com.migration.example.migrationscript.controller;

import com.migration.example.migrationscript.UserService;
import com.migration.example.migrationscript.migration.MigrationRunnable;
import com.migration.example.migrationscript.mongo.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {
    private final static int THREAD_POOL_SIZE = 5;
    private final static int BATCH_SIZE = 1000;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @GetMapping("/process")
    public String processData() {
        // Call UserService method
        userService.fetchDataAndStoreUserIds();
        return "Ids fetched from db and stored in file .";
    }

    @GetMapping("/fetchGameData")
    public void fetchGameData() {
        long startTime = System.currentTimeMillis();

        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
        String directoryPath = null;
        try {
            directoryPath = Paths.get(url.toURI()).getParent().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (directoryPath != null) {
            String filename = "userids.txt";

            // Combine directory path and filename to get the full file path
            String filePath = directoryPath + File.separator + filename;

            System.out.println("filepath: " + filePath);

            // Loop until no user IDs are found
            while (true) {
                // Read user IDs from the file
                List<Integer> userIds = userService.readUserIdFromFile(filePath, 1000);

                if (userIds == null || userIds.isEmpty()) {
                    System.out.println("No User ID found in the file.");
                    break;
                }
                // Check if user IDs are found

                // Fetch game data for the user IDs
                userService.fetchGameData(userIds);

                // Remove processed user IDs from the file
                userService.removeUserIdFromFile(filePath, userIds.size());
                System.out.println("Fetched game data for user IDs: " + userIds);

            }
            System.out.println("Processing completed.");
        } else {
            System.out.println("Error: Unable to determine directory path.");
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("total execution took (in api call) " + duration);
    }


    @GetMapping("/fetchGameData2")
    public LocalDateTime fetchGameData2() {
        scheduleJob();
        return LocalDateTime.now();
    }

    private void scheduleJob() {
        ExecutorService executorService = Executors.newWorkStealingPool();
        try {
            while (true) {
                long start = System.currentTimeMillis();
                List<Integer> userIdList = userService.fetchAndRemoveUserIds(getFilePath(), BATCH_SIZE);
                long end = System.currentTimeMillis();
                System.out.println("Total Time Taken to write on Disk (in ms): " + (end-start));
                if (userIdList.isEmpty()) {
                    break;  // No more user IDs to process
                }
                executorService.submit(new MigrationRunnable(userIdList, userService));
            }
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void getUserIds(int count) {
        List<Integer> userIdList = userService.fetchAndRemoveUserIds(getFilePath(), count);
        int size = userService.fetchGameDataWithSize(userIdList);

        if (userIdList.size() != size) {
            System.out.println("Something went wrong in the current batch");
            System.out.println(userIdList);
        }
    }

    private String getFilePath() {
        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
        String directoryPath = null;
        try {
            directoryPath = Paths.get(url.toURI()).getParent().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String filename = "userids.txt";

        return directoryPath + File.separator + filename;
    }

    @PostMapping("/startKafkaConsumer")
    public LocalDateTime startKafkaConsumer() {
        kafkaConsumer.startConsuming();
        System.out.println("Kafka consumer started.");
        return LocalDateTime.now();
    }

    @PostMapping("/stopKafkaConsumer")
    public void stopKafkaConsumer() {
        kafkaConsumer.stopConsuming();
        System.out.println("Kafka consumer stopped.");
    }

    @PostMapping ("/matchData")
    public void getUserData(@RequestParam List<Integer> userIds) {
        userService.dataFromSQLandMongo(userIds);
    }

    @PostMapping("/fetchGameDataAsPerNumber")
    public void configureMaxPollRecords(@RequestParam int maxPollRecords) {
        long startTime = System.currentTimeMillis();

        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
        String directoryPath = null;
        try {
            directoryPath = Paths.get(url.toURI()).getParent().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (directoryPath != null) {
            String filename = "userids.txt";

            // Combine directory path and filename to get the full file path
            String filePath = directoryPath + File.separator + filename;

            System.out.println("filepath: " + filePath);

            int i =0;
            // Loop until no user IDs are found
            while (i< maxPollRecords) {
                // Read user IDs from the file
                List<Integer> userIds = userService.readUserIdFromFile(filePath, 1000);

                if (userIds == null || userIds.isEmpty()) {
                    System.out.println("No User ID found in the file.");
                    break;
                }
                // Check if user IDs are found

                // Fetch game data for the user IDs
                userService.fetchGameData(userIds);

                // Remove processed user IDs from the file
                userService.removeUserIdFromFile(filePath, userIds.size());
                System.out.println("Fetched game data for user IDs: " + userIds);
                i++;

            }
            System.out.println("Processing completed.");
        } else {
            System.out.println("Error: Unable to determine directory path.");
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("total execution took (in api call) " + duration);
    }
}
