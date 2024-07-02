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

    public List<Integer> fetchAndRemoveUserIds(String filePath, int count) {
        List<Integer> userIds = new ArrayList<>(count);
        StringBuilder remainingLines = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int linesProcessed = 0;

            while ((line = reader.readLine()) != null) {
                if (linesProcessed < count) {
                    try {
                        userIds.add(Integer.parseInt(line.trim()));
                        linesProcessed++;
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid line: " + line);
                    }
                } else {
                    remainingLines.append(line).append(System.lineSeparator());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(remainingLines.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userIds;
    }

}
