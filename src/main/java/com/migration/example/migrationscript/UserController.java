package com.migration.example.migrationscript;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
        String directoryPath = null;
        try {
            directoryPath = Paths.get(url.toURI()).getParent().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (directoryPath != null) {
            // Define the filename
            String filename = "userids.txt";

            // Combine directory path and filename to get the full file path
            String filePath = directoryPath + File.separator + filename;

            System.out.println("filepath: " + filePath);

            // Loop until no user IDs are found
            while (true) {
                // Read user IDs from the file
                List<Integer> userIds = userService.readUserIdFromFile(filePath, 100);

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
    }


    @PostMapping("/startKafkaConsumer")
    public void startKafkaConsumer() {
        kafkaConsumer.startConsuming();
        System.out.println("Kafka consumer started.");
    }

    @PostMapping("/stopKafkaConsumer")
    public void stopKafkaConsumer() {
        kafkaConsumer.stopConsuming();
        System.out.println("Kafka consumer stopped.");
    }

}
