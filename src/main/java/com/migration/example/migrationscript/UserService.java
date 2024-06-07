package com.migration.example.migrationscript;

import com.migration.example.migrationscript.UserIdFileReader;
import com.migration.example.migrationscript.UserIdFileWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private  DataSource dataSource;
    @Autowired
    private  UserIdFileWriter userIdFileWriter;
    @Autowired
    private  UserIdFileReader userIdFileReader;

    @Autowired
    private GameDataFetcher gameDataFetcher;


    public void fetchDataAndStoreUserIds() {
        try (Connection connection = dataSource.getConnection()) {
            userIdFileWriter.fetchAndStoreUserIds(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }

    public List<Integer> readUserIdFromFile(String filePath, int count) {
        return userIdFileReader.fetchUserIds(filePath,count);
    }

    public void removeUserIdFromFile(String filePath,int count) {
        userIdFileReader.removeUserIds(filePath,count);
    }
    public void fetchGameData(List<Integer> userIds) {
//        // Implement the logic to fetch game data for the given userId
//        // Example:
//        System.out.println("Fetching game data for user ID: " + userId);
        gameDataFetcher.fetchGameData(userIds);

    }
}
