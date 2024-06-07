package com.migration.example.migrationscript;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Component
public class GameDataFetcher {
    private static final String topicName = "dataForMongo3";

    @Autowired
    private  DataSource dataSource;

    @Autowired
    private JsonConverter jsonConverter;

    @Autowired
    private KafkaPublisher kafkaPublisher;


        public void fetchGameData(List<Integer> userIds) {
            if (userIds.isEmpty()) {
                return; // No user IDs to process
            }

            // Create a comma-separated list of question marks for the IN clause
            String inClause = String.join(",", Collections.nCopies(userIds.size(), "?"));

            String query = "SELECT * FROM gamebyvariant WHERE userid IN (" + inClause + ") ORDER BY userid";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // Set the user IDs in the prepared statement
                for (int i = 0; i < userIds.size(); i++) {
                    statement.setInt(i + 1, userIds.get(i));
                }

                ResultSet resultSet = statement.executeQuery();

                Map<Integer, UserData> userDataMap = new HashMap<>();

                while (resultSet.next()) {
                    int userId = resultSet.getInt("userid");
                    String gameVariant = resultSet.getString("gamevariant");
                    double entryFee = resultSet.getDouble("entryfee");

                    UserData userData = userDataMap.computeIfAbsent(userId, k -> new UserData());

                    // Set entry fee based on game variant
                    switch (gameVariant) {
                        case "Points":
                            if (userData.getPOINTS() == null) userData.setPOINTS(new ArrayList<>());
                            userData.getPOINTS().add(entryFee);
                            break;
                        case "Points21":
                            if (userData.getPOINTS21() == null) userData.setPOINTS21(new ArrayList<>());
                            userData.getPOINTS21().add(entryFee);
                            break;
                        case "Points10":
                            if (userData.getPOINTS10() == null) userData.setPOINTS10(new ArrayList<>());
                            userData.getPOINTS10().add(entryFee);
                            break;
                        case "Pool61":
                            if (userData.getPOOL61() == null) userData.setPOOL61(new ArrayList<>());
                            userData.getPOOL61().add(entryFee);
                            break;
                        case "Pool101":
                            if (userData.getPOOL101() == null) userData.setPOOL101(new ArrayList<>());
                            userData.getPOOL101().add(entryFee);
                            break;
                        case "Pool201":
                            if (userData.getPOOL201() == null) userData.setPOOL201(new ArrayList<>());
                            userData.getPOOL201().add(entryFee);
                            break;
                        case "Pool":
                            if (userData.getPOOL() == null) userData.setPOOL(new ArrayList<>());
                            userData.getPOOL().add(entryFee);
                            break;
                        case "Deal":
                            if (userData.getDEAL() == null) userData.setDEAL(new ArrayList<>());
                            userData.getDEAL().add(entryFee);
                            break;
                        case "DealSNG":
                            if (userData.getDEALSNG() == null) userData.setDEALSNG(new ArrayList<>());
                            userData.getDEALSNG().add(entryFee);
                            break;
                        case "KnockOut":
                            if (userData.getKNOCKOUT() == null) userData.setKNOCKOUT(new ArrayList<>());
                            userData.getKNOCKOUT().add(entryFee);
                            break;
                        default:
                            break;
                    }

                    userData.setUserId(userId);
                    userData.setCreatedAt(new Date());
                    userData.setUpdatedAt(new Date());
                }

                // Convert each UserData to JSON and publish to Kafka
                for (UserData userData : userDataMap.values()) {
                    //System.out.println("user's data fetched"+userData);
                    String json = jsonConverter.convertToJson(userData);
                    //System.out.println("user's data json"+json);
                    int userid = userData.getUserId();
                    kafkaPublisher.publishToKafka(topicName,String.valueOf(userid), json);
                }

            } catch (SQLException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }


}
