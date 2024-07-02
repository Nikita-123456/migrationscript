package com.migration.example.migrationscript;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.migration.example.migrationscript.repository.GameByVariantRepository;
import com.migration.example.migrationscript.repository.UsersWinningByGamevariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UserIdFileWriter {
    @Autowired
    GameByVariantRepository repo;
    @Autowired
    private DataSource dataSource;
    @Autowired
    UsersWinningByGamevariantRepository usersWinningByGamevariantRepository;

    public void fetchAndStoreUserIds() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();

            long startTime = System.currentTimeMillis();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT UserId FROM UsersWinningByGamevariant");
            //ResultSet resultSet = statement.executeQuery("SELECT  UserId FROM UsersWinningByGamevariant WHERE UserId = 1982048");
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UsersWinningByGamevariant select query took " + duration + " milliseconds");

            long start = System.currentTimeMillis();
            List<Integer> userIds = new ArrayList<>();
            while (resultSet.next()) {
                userIds.add(resultSet.getInt("UserId"));
            }
            long end = System.currentTimeMillis();
            System.out.println("list creation took: " + (end - start));

            // Get the directory path where the class file is located
            URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
            String directoryPath = null;
            try {
                directoryPath = Paths.get(url.toURI()).getParent().toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            startTime = System.currentTimeMillis();
            if (directoryPath != null) {
                // Define the filename
                String filename = "userids.txt";

                // Combine directory path and filename to get the full file path
                String filePath = directoryPath + File.separator + filename;

                FileWriter writer = new FileWriter(filePath);
                int count = 0;
                for (Integer userId : userIds) {
                    writer.write(userId.toString() + "\n");
                    count++;

                }
                writer.close();

                endTime = System.currentTimeMillis();
                duration = endTime - startTime;
                System.out.println("writing in file took " + duration + " milliseconds");
                System.out.println("Number of user IDs written to file: " + count);

            } else {
                System.err.println("Error: Unable to determine directory path.");
            }


        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
