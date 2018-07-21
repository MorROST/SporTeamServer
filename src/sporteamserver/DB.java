/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sporteamserver;

import com.yonimor.sporteam.sporteam.com.data.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;








/**
 *
 * @author TheYoni
 */
public class DB {
    public static final String DB_URL = "jdbc:derby://localhost:1527/SporTeamDB";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "moni";
    public static final String DB_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    public static Connection conn;
    public static Statement statement;
    public static PreparedStatement pstUsers;
    public static PreparedStatement pstGame;
    public static PreparedStatement pstMessaging;
    public static PreparedStatement pstUpdateMessaging;
    public static PreparedStatement pstSetToGroupTable;
    
    private static final int constLogin = 1;
    private static final int constRegister = 2;
    private static final int constToken = 3;
    private static final int constGroupTable = 4;
    
    private static final String InsertUserSQL = "INSERT into SPORTEAMUSERS values(?,?,?,?,?,?)";
    private static final String InsertGameSQL = "INSERT into SPORTEAMGAMES values(?,?,?,?,?,?,?,?)";
    private static final String InsertMessagingSQL = "INSERT into MESSAGING values(?,?)";
    private static final String UpdateMessagingSQL = "UPDATE MESSAGING set EMAIL = ?, Token = ? WHERE EMAIL = ?";
    private static final String InsertGroupSQL = "INSERT into SPORTEAMGROUPS values(?,?)";
    
    private ArrayList<String> extraParameters = new ArrayList<String>();
    private int lastgame=1;
    
    public DB()
    {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            pstUsers = conn.prepareStatement(InsertUserSQL);
            pstGame = conn.prepareStatement(InsertGameSQL);
            pstMessaging = conn.prepareStatement(InsertMessagingSQL);
            pstUpdateMessaging = conn.prepareStatement(UpdateMessagingSQL);
            pstSetToGroupTable = conn.prepareStatement(InsertGroupSQL);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT GameNumber FROM SPORTEAMGAMES ORDER BY GameNumber DESC");
            rs.next();
            lastgame = rs.getInt("GameNumber");
            DeleteOldGames();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            lastgame=0;
        }
    }
    
    public String LogIn(String email, String password)
    {
        if(IsAlreadyExiest("", password, email, 0, constLogin) == ConnectionData.OK)
        {
            String userNameReturn = extraParameters.get(0);
            extraParameters.clear();
            return userNameReturn;
        }
        return "";
    }
    
    public int Register(User u)
    {
        if(IsAlreadyExiest("", "", u.getEmail(), 0, constRegister) == ConnectionData.OK)
        {
            return ConnectionData.NOT_OK;
        }
        else{
            try {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                pstUsers.setString(1, u.getUserName());
                pstUsers.setString(2, u.getPassword());
                pstUsers.setString(3, u.getEmail());
                pstUsers.setString(4, u.getGender());
                pstUsers.setInt(5, u.getAge());
                pstUsers.setString(6, u.getPhone());
                pstUsers.execute();

                conn.close();
                System.out.println(u.getUserName() + " added");
                return ConnectionData.OK;
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
                return ConnectionData.SOMTHING_WRONG;
            }
        }
    }
    
    public int InsertGame(Game g)
    {
        try {
            lastgame++;
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            pstGame.setInt(1, lastgame);
            pstGame.setString(2, g.getCreatedBy());
            pstGame.setString(3, g.getSportType());
            pstGame.setString(4, g.getTime());
            pstGame.setString(5, g.getDate());
            pstGame.setString(6, g.getCity());
            pstGame.setInt(7, g.getNumberOfPlayers());
            pstGame.setString(8, g.getLoaction());
            pstGame.execute();
            
            conn.close();
            int result = InsertToGroupTable(g.getCreatedBy(), lastgame);
            if (result == ConnectionData.OK)
            {
                return ConnectionData.OK;
            }
            else 
                return ConnectionData.SOMTHING_WRONG;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return ConnectionData.SOMTHING_WRONG;
        }
            
    }
    
    public ArrayList GetGames(int gameNumber)
    {
        ArrayList<Game> arr = new ArrayList<Game>();
        Statement st;
        try {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        st = conn.createStatement();
        Statement stselect = conn.createStatement();
        ResultSet rs = stselect.executeQuery("select * from SPORTEAMGAMES where GAMENUMBER > " + gameNumber);
        while (rs.next())
            {
                Game g = new Game(rs.getString("CreatedBy"), rs.getString("SportType"), rs.getString("City"),
                rs.getString("GameTime"), rs.getString("GameDate"), rs.getString("GameLocation"), rs.getInt("NumberOfParticipant"));
                g.setGameNumber(rs.getInt("GAMENUMBER"));
                arr.add(g);
                
            }
        rs.close();
        conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }
    
    public void DeleteOldGames()
    {
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
        @Override
        public void run () {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            String[] timeParts=null, DatePartsLocal,DatePartsDB;
            Statement st;
            try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            st = conn.createStatement();
            String myDate = df.format(cal.getTime());
            PreparedStatement stselect = conn.prepareStatement("select * from SPORTEAMGAMES where GameDate = ?", ResultSet.TYPE_SCROLL_SENSITIVE,
          ResultSet.CONCUR_UPDATABLE);
            stselect.setObject(1, myDate);
            DatePartsLocal = myDate.split("/");
            
            ResultSet rs = stselect.executeQuery();
            while (rs.next())
                {
                    timeParts = rs.getString("GameTime").split(":");
                    if(cal.get(Calendar.HOUR_OF_DAY)>Integer.parseInt(timeParts[0]))
                    {
                        rs.deleteRow();
                    }  
                }
            rs.close();
            conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    // schedule the task to run starting now and then every hour...
    timer.schedule (hourlyTask, 0l, 1000*60*60);
    }
    
    public int SaveProfileImage(String image, String name)
    {
        try {
            byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(image);
            String location = "images/" + name +".png";
            File of = new File(location);
            FileOutputStream osf = new FileOutputStream(of);
            osf.write(btDataFile);
            osf.flush();
        } catch (IOException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
    
    public String GetProfilePicture(String name)
    {
        try {
            String dir = "images/" + name + ".png";
            File myFile = new File(dir);
            FileInputStream imageInFile = new FileInputStream(myFile);
            byte imageData[] = new byte[(int) myFile.length()];
            imageInFile.read(imageData);
            String imageDataString;
            imageDataString = Base64.getEncoder().encodeToString(imageData);
            return imageDataString;

 
        } catch (IOException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        
    }
    
    public int InsertToken(String email, String token)
    {
        int isNew = IsAlreadyExiest("", "", email, 0, constToken);
        if (isNew == ConnectionData.NOT_OK)
        {
            try {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                pstMessaging.setString(1, email);
                pstMessaging.setString(2, token);
                pstMessaging.execute();

                conn.close();
                return ConnectionData.OK;
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
                return ConnectionData.SOMTHING_WRONG;
            }
        }
        else if(isNew == ConnectionData.OK)
        {
            try {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);                
                pstUpdateMessaging.setString(1, email);
                pstUpdateMessaging.setString(2, token);
                pstUpdateMessaging.setString(3, email);
                pstUpdateMessaging.executeUpdate();
                return ConnectionData.OK;
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
                return ConnectionData.SOMTHING_WRONG;
            }
        }
        return ConnectionData.SOMTHING_WRONG;
    }
    
    public int InsertToGroupTable(String email, int gameNumber)
    {
        int isNew = IsAlreadyExiest("", "", email, gameNumber, constGroupTable);
        if (isNew == ConnectionData.NOT_OK)
            try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            pstSetToGroupTable.setString(1, email);
            pstSetToGroupTable.setInt(2, gameNumber);
            pstSetToGroupTable.execute();
            
            conn.close();
            return ConnectionData.OK;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        else if (isNew == ConnectionData.OK)
        {
            return ConnectionData.NOT_OK;
        }
        return ConnectionData.SOMTHING_WRONG;
    }
    
    public void NotifyToOther(String name, int gameNumber, int reason)
    {
        Statement stselectGame;
        Statement stselectToken;
        try {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        ResultSet result = null;
        ResultSet rs = null;
        ArrayList toList = new ArrayList();
        String OtherPlayer = "";
        String to = "";
        String title = name + " has joind the game";
        stselectGame = conn.createStatement();
        stselectToken = conn.createStatement();
        rs = stselectGame.executeQuery("select * from SporTeamGroups where GameNumber = " + gameNumber);
        while (rs.next())
            {
                OtherPlayer = rs.getString("EMAIL");
                result = stselectToken.executeQuery("select TOKEN from Messaging where EMAIL = '" + OtherPlayer + "'");
                result.next();
                to = result.getString("TOKEN");
                Notification.SendNotification(to, title);
            }
        result.close();
        rs.close();
        conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public int IsAlreadyExiest(String name, String password, String email, int gameNumber, int whatToCheck)
    {
        Statement st;
        int returnedResult = ConnectionData.NOT_OK;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            st = conn.createStatement();
            Statement stselect = conn.createStatement();
            switch (whatToCheck)
            {
                case constLogin:
                {
                    ResultSet rs = stselect.executeQuery("select * from SporTeamUsers");
                    while (rs.next())
                    {
                        String emailReturn = rs.getString("EMAIL");
                        String passwordReturn = rs.getString("Password");
                        if (email.equals(emailReturn) && password.equals(passwordReturn))
                        {
                            String userNameReturn = rs.getString("UserName");
                            extraParameters.add(userNameReturn);
                            rs.close();
                            conn.close();
                            returnedResult = ConnectionData.OK;
                            break;
                        }
                    }
                    break;
                }
                case constRegister:
                {
                    ResultSet rs = stselect.executeQuery("select * from SporTeamUsers");
                    while (rs.next())
                    {
                        String emailReturn = rs.getString("EMAIL");
                        if (email.equals(emailReturn))
                        {
                            rs.close();
                            conn.close();
                            returnedResult = ConnectionData.OK;
                            break;
                        }
                    }                    
                    break;
                }
                case constToken:
                {
                    ResultSet rs = stselect.executeQuery("select * from MESSAGING where EMAIL LIKE '" + email + "'");
                    while (rs.next())
                        {
                            returnedResult = ConnectionData.OK;
                            break;
                        }
                    rs.close();
                    conn.close();
                    break;
                }
                case constGroupTable:
                {
                    ResultSet rs = stselect.executeQuery("select * from SPORTEAMGROUPS where EMAIL LIKE '" + email + "' AND GAMENUMBER = " + gameNumber);
                    while (rs.next())
                        {
                            returnedResult = ConnectionData.OK;
                            break;
                        }
                    rs.close();
                    conn.close();
                    break;                    
                }
            }
            
        }
        catch (Exception e)
        {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            return ConnectionData.SOMTHING_WRONG;
        }
        return returnedResult;
    }
}
