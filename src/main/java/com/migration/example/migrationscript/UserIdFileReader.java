package com.migration.example.migrationscript;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserIdFileReader {

    public List<Integer> fetchUserIds(String filePath, int count) {
        List<Integer> userIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null && userIds.size() < count) {
                userIds.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userIds;
    }

    public void removeUserIds(String filePath, int count) {
        try {
            File file = new File(filePath);
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            if (lines.size() >= count) {
                lines.subList(0, count).clear();
                try (FileWriter writer = new FileWriter(file)) {
                    for (String line : lines) {
                        writer.write(line + System.lineSeparator());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
