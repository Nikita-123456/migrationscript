package com.migration.example.migrationscript;

import com.migration.example.migrationscript.UserIdFileReader;
import com.migration.example.migrationscript.UserIdFileWriter;
import com.migration.example.migrationscript.model.UserData;
import com.migration.example.migrationscript.mongo.MongoDataFetch;
import org.springframework.asm.Handle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Service
public class UserService {
    @Autowired
    private  UserIdFileWriter userIdFileWriter;
    @Autowired
    private  UserIdFileReader userIdFileReader;

    @Autowired
    private MongoDataFetch mongoDataFetch;

    @Autowired
    private GameDataFetcher gameDataFetcher;


    public void fetchDataAndStoreUserIds() {
//        try (Connection connection = dataSource.getConnection()) {
            userIdFileWriter.fetchAndStoreUserIds();
//        } catch (SQLException e) {
//            e.printStackTrace();
//             Handle SQL exception
//        }
    }

    public List<Integer> readUserIdFromFile(String filePath, int count) {
        return userIdFileReader.fetchUserIds(filePath,count);
    }

    public void removeUserIdFromFile(String filePath,int count) {
        userIdFileReader.removeUserIds(filePath,count);
    }

    public List<Integer> fetchAndRemoveUserIds(String filePath, int count) {
        return userIdFileReader.fetchAndRemoveUserIds(filePath, count);
    }
    public void fetchGameData(List<Integer> userIds) {
        gameDataFetcher.fetchGameData(userIds);

    }

    public int fetchGameDataWithSize(List<Integer> userIds) {
        return gameDataFetcher.fetchGameDataWithSize(userIds);
    }

//    public void dataFromMongo(List<Integer> userIds){
//        gameDataFetcher.fetchDataFromMongoAsPerUserId(userids);
//    }

    public void dataFromSQLandMongo(List<Integer> userIds){

        Map<Integer, UserData> sqlData= gameDataFetcher.fetchDataFromSQLAsPerUserId(userIds);

        Map<Integer, UserData> mongoData= mongoDataFetch.fetchDataFromMongoAsPerUserId(userIds);


        for (Integer userId : userIds) {
            UserData sqlUserData = sqlData.get(userId);
            UserData mongoUserData = mongoData.get(userId);

//            System.out.println("Data SQL for user ID: " + userId + "data" + sqlUserData.toString());
//            System.out.println("Data MONGO for user ID: " + userId + "data" + mongoUserData.toString());

            if (sqlUserData != null && mongoUserData != null) {
                if (!sqlUserData.equalsIgnoringTimestamps(mongoUserData)) {
                    System.out.println("Data mismatch for user ID: " + userId);
                }else{
                    System.out.println("Data matched for user ID: " + userId);
                }
            } else if (sqlUserData == null) {
                System.out.println("SQL data missing for user ID: " + userId);
            } else if (mongoUserData == null) {
                System.out.println("MongoDB data missing for user ID: " + userId);
            }
        }




    }
}
