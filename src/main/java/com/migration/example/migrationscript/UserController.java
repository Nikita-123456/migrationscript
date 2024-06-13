package com.migration.example.migrationscript;

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
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UserController {


    @Autowired
    private  UserService userService;

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
