package com.yonimor.sporteam.sporteam.com.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TheYoni on 11/11/2017.
 */

public class Game implements Serializable {
    String createdBy, sportType,city,time,date,loaction;
    int numberOfPlayers;
    int gameNumber;



    ArrayList<String> players;

    public Game(String createdBy, String sportType, String city, String time, String date, String loaction, int numberOfPlayers)
    {
        this.createdBy = createdBy;
        this.sportType = sportType;
        this.city = city;
        this.time = time;
        this.date = date;
        this.loaction = loaction;
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<String>();
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoaction() {
        return loaction;
    }

    public void setLoaction(String loaction) {
        this.loaction = loaction;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void AddPlayer(String playerName)
    {
        this.players.add(playerName);
    }
    public void RemovePlayer(String playerName)
    {
        this.players.remove(playerName);
    }
    public ArrayList<String> getPlayers() {
        return players;
    }
}