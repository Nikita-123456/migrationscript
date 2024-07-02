package com.migration.example.migrationscript.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "UsersWinningByGamevariant")
public class UsersWinningByGamevariant {

    @Id
    public Long userid;
    public double pool101;
    public double pool201;
    public double points;
    public double points21;
    public double deal;
    public double pool;
    public String lastupdateddate;
    public double dealsng;
    public double pool61;
    public double knockout;

}