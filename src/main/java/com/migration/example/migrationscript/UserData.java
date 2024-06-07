package com.migration.example.migrationscript;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

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
}
