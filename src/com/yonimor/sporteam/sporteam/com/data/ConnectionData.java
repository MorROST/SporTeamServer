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
   public static final int LOGIN = 1;
   public static final int REGISTER = 2;
    
    private int requestCode;
    private String email, password;
    private boolean worked;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public boolean isWorked() {
        return worked;
    }

    public void setWorked(boolean worked) {
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