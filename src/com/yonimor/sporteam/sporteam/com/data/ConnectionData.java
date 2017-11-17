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
    public static final int UPDATEGAMES = 8;
    
    private int requestCode;
    private String email, password;
    private int worked;
    private User user;
    private Game game;
    private ArrayList arrayList;
    private int lastGameAtClient;

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