package com.migration.example.migrationscript.mongo;

import com.migration.example.migrationscript.model.UserData;
import com.migration.example.migrationscript.repository.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MongoDataFetch {

    @Autowired
    private UserMongoRepository userRepository;

    public Map<Integer, UserData> fetchDataFromMongoAsPerUserId(List<Integer> userIds){
        List<UserData> userDataList = userRepository.findByUserIdIn(userIds);

        // Create a map to store the user data with userId as the key
        Map<Integer, UserData> userDataMap = new HashMap<>();
        for (UserData userData : userDataList) {
            userDataMap.put(userData.getUserId(), userData);
        }

        return userDataMap;
    }

}
