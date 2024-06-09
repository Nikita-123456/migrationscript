package com.migration.example.migrationscript.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "gamebyvariant")
public class GameByVariant {

    @Column("userid")
    private int userId;
    @Column("entryfee")
    private double entryFee;
    @Column("gamevariant")
    private String gameVariant;
    @Column("createdDate")
    private String createdAt;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(double entryFee) {
        this.entryFee = entryFee;
    }

    public String getGameVariant() {
        return gameVariant;
    }

    public void setGameVariant(String gameVariant) {
        this.gameVariant = gameVariant;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
