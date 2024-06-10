package com.migration.example.migrationscript.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "game_fee_by_variant_collection")
public class UserData {
    @Id
    private int userId;
    private List<Double> POINTS;
    private List<Double> POINTS21;
    private List<Double> POINTS10;
    private List<Double> POOL61;
    private List<Double> POOL101;
    private List<Double> POOL201;
    private List<Double> POOL;
    private List<Double> DEAL;
    private List<Double> DEALSNG;
    private List<Double> KNOCKOUT;
    private Date createdAt;
    private Date updatedAt;

    public UserData() {

    }

    public UserData(int userId) {
        this.userId = userId;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Double> getPOINTS() {
        return POINTS;
    }

    public void setPOINTS(List<Double> POINTS) {
        this.POINTS = POINTS;
    }

    public List<Double> getPOINTS21() {

        return POINTS21;
    }

    public void setPOINTS21(List<Double> POINTS21) {
        this.POINTS21 = POINTS21;
    }

    public List<Double> getPOINTS10() {
        return POINTS10;
    }

    public void setPOINTS10(List<Double> POINTS10) {
        this.POINTS10 = POINTS10;
    }

    public List<Double> getPOOL61() {
        return POOL61;
    }

    public void setPOOL61(List<Double> POOL61) {
        this.POOL61 = POOL61;
    }

    public List<Double> getPOOL101() {
        return POOL101;
    }

    public void setPOOL101(List<Double> POOL101) {
        this.POOL101 = POOL101;
    }

    public List<Double> getPOOL201() {
        return POOL201;
    }

    public void setPOOL201(List<Double> POOL201) {
        this.POOL201 = POOL201;
    }

    public List<Double> getPOOL() {
        return POOL;
    }

    public void setPOOL(List<Double> POOL) {
        this.POOL = POOL;
    }

    public List<Double> getDEAL() {
        return DEAL;
    }

    public void setDEAL(List<Double> DEAL) {
        this.DEAL = DEAL;
    }

    public List<Double> getDEALSNG() {
        return DEALSNG;
    }

    public void setDEALSNG(List<Double> DEALSNG) {
        this.DEALSNG = DEALSNG;
    }

    public List<Double> getKNOCKOUT() {
        return KNOCKOUT;
    }

    public void setKNOCKOUT(List<Double> KNOCKOUT) {
        this.KNOCKOUT = KNOCKOUT;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }



    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId=" + userId +
                ", POINTS=" + POINTS +
                ", POINTS21=" + POINTS21 +
                ", POINTS10=" + POINTS10 +
                ", POOL61=" + POOL61 +
                ", POOL101=" + POOL101 +
                ", POOL201=" + POOL201 +
                ", POOL=" + POOL +
                ", DEAL=" + DEAL +
                ", DEALSNG=" + DEALSNG +
                ", KNOCKOUT=" + KNOCKOUT +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }


    public boolean equalsIgnoringTimestamps(UserData other) {

        System.out.println("Data other: " +other);

        System.out.println("Data THIS: " +this);


        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return Objects.equals(userId, other.userId) &&
                Objects.equals(POINTS, other.POINTS) &&
                Objects.equals(POINTS21, other.POINTS21) &&
                Objects.equals(POINTS10, other.POINTS10) &&
                Objects.equals(POOL61, other.POOL61) &&
                Objects.equals(POOL101, other.POOL101) &&
                Objects.equals(POOL201, other.POOL201) &&
                Objects.equals(POOL, other.POOL) &&
                Objects.equals(DEAL, other.DEAL) &&
                Objects.equals(DEALSNG, other.DEALSNG) &&
                Objects.equals(KNOCKOUT, other.KNOCKOUT);
    }

}
