package com.migration.example.migrationscript.migration;

import com.migration.example.migrationscript.UserService;

import java.util.List;

public class MigrationRunnable implements Runnable{
    private final List<Integer> userIdList;
    private final UserService userService;

    public MigrationRunnable(List<Integer> userIdList, UserService userService) {
        this.userIdList = userIdList;
        this.userService = userService;
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        int size = userService.fetchGameDataWithSize(userIdList);
        if (userIdList.size() != size) {
            System.out.println("Something went wrong in the current batch");
            System.out.println(userIdList);
        }
    }
}
