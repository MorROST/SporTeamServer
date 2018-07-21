/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yonimor.sporteam.sporteam.com.data;





import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author TheYoni
 */

public class ConnectionData implements Serializable{
    public static final int OK = 1;
    public static final int NOT_OK = 2;
    public static final int SOMTHING_WRONG = 3;
    public static final int LOGIN = 4;
    public static final int REGISTER = 5;
    public static final int INSERTGAME = 6;
    public static final int ALLGAMES = 7;
    public static final int UPLOADIMAGE = 8;
    public static final int GETIMAGE = 9;
    public static final int SETTOKEN = 10;
    public static final int JOINGAME = 11;



    private int requestCode;
    private String email="", password="";
    private int worked=0;
    private User user;
    private Game game;
    private ArrayList arrayList;
    private int lastGameAtClient=0;
    private String name="";
    private String stringImage;
    private String token;
    private int gameNumber;

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStringImage() {
        return stringImage;
    }

    public void setStringImage(String stringImage) {
        this.stringImage = stringImage;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLastGameAtClient() {
        return lastGameAtClient;
    }

    public void setLastGameAtClient(int lastGameAtClient) {
        this.lastGameAtClient = lastGameAtClient;
    }

    public ArrayList getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList arrayList) {
        this.arrayList = arrayList;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getWorked() {
        return worked;
    }

    public void setWorked(int worked) {
        this.worked = worked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}