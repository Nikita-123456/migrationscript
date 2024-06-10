package com.migration.example.migrationscript;

import com.migration.example.migrationscript.model.GameByVariant;
import com.migration.example.migrationscript.model.UserData;
import com.migration.example.migrationscript.repository.GameByVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class GameDataFetcher {

    @Autowired
    private  DataSource dataSource;

    @Autowired
    private KafkaPublisher kafkaPublisher;

    @Autowired
    GameByVariantRepository gameByVariantRepository;

    public void fetchGameData(List<Integer> userIds) {
        if (userIds.isEmpty())
            return;

        long startTime = System.currentTimeMillis();

        List<GameByVariant> list = gameByVariantRepository.findByUserIdIn(userIds);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("execution took (in query execution) " + duration);
        Map<Integer, List<GameByVariant>> userGamePlayList = list.parallelStream()
                                                            .collect(Collectors.groupingBy(GameByVariant::getUserId));

        ConcurrentMap<Integer, UserData> userDataMap = new ConcurrentHashMap<>();
        // Process each group in parallel
        userGamePlayList.entrySet().parallelStream()
        .forEach(entry -> {
            int userId = entry.getKey();
            List<GameByVariant> variants = entry.getValue();
            UserData userData = userDataMap.computeIfAbsent(userId, UserData::new);
            variants.forEach(e -> addEntryFeeToUserData(userData, e.getGameVariant(), e.getEntryFee()));
        });

        kafkaPublisher.publishToKafka(userDataMap);

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("execution took (in kafka event production) " + duration);

    }

    private void addEntryFeeToUserData(UserData userData, String gameVariant, double entryFee) {
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
    }

    public Map<Integer, UserData>  fetchDataFromSQLAsPerUserId (List<Integer> userIds){
        List<GameByVariant> list = gameByVariantRepository.findByUserIdIn(userIds);

        Map<Integer, List<GameByVariant>> userGamePlayList = list.parallelStream()
                .collect(Collectors.groupingBy(GameByVariant::getUserId));

        ConcurrentMap<Integer, UserData> userDataMap = new ConcurrentHashMap<>();
        // Process each group in parallel
        userGamePlayList.entrySet().parallelStream()
                .forEach(entry -> {
                    int userId = entry.getKey();
                    List<GameByVariant> variants = entry.getValue();
                    UserData userData = userDataMap.computeIfAbsent(userId, UserData::new);
                    variants.forEach(e -> addEntryFeeToUserData(userData, e.getGameVariant(), e.getEntryFee()));
                });
        return userDataMap;
    }

}
