/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sporteamserver;

import com.yonimor.sporteam.sporteam.com.data.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;





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
    
    public static final String InsertUserSQL = "INSERT into SPORTEAMUSERS values(?,?,?,?,?)";
    public static final String InsertGameSQL = "INSERT into SPORTEAMGAMES values(?,?,?,?,?,?,?,?)";
    
    private int lastgame=1;
    
    public DB()
    {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            pstUsers = conn.prepareStatement(InsertUserSQL);
            pstGame = conn.prepareStatement(InsertGameSQL);
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String LogIn(String email, String password)
    {
        Statement st;
        String emailReturn;
        String passwordReturn;
        String userNameReturn;
        
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            st = conn.createStatement();
            Statement stselect = conn.createStatement();
            ResultSet rs = stselect.executeQuery("select * from SporTeamUsers");
            while (rs.next())
            {
                emailReturn = rs.getString("EMAIL");
                passwordReturn = rs.getString("Password");
                if (email.equals(emailReturn) && password.equals(passwordReturn))
                {
                    userNameReturn = rs.getString("UserName");
                    rs.close();
                    conn.close();
                    return userNameReturn;
                }
            }
            
            return "";
            
        } catch (SQLException ex) {
            return "";
        }
    }
    
    public int Register(User u)
    {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            pstUsers.setString(1, u.getUserName());
            pstUsers.setString(2, u.getPassword());
            pstUsers.setString(3, u.getEmail());
            pstUsers.setString(4, u.getGender());
            pstUsers.setInt(5, u.getAge());
            pstUsers.execute();
            
            conn.close();
            System.out.println(u.getUserName() + " added");
            return ConnectionData.OK;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return ConnectionData.SOMTHING_WRONG;
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
            return ConnectionData.OK;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return ConnectionData.SOMTHING_WRONG;
        }
            
    }
    
    public ArrayList GetGames()
    {
        ArrayList<Game> arr = new ArrayList<Game>();
        Statement st;
        try {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        st = conn.createStatement();
        Statement stselect = conn.createStatement();
        ResultSet rs = stselect.executeQuery("select * from SPORTEAMGAMES");
        while (rs.next())
            {
                Game g = new Game(rs.getString("CreatedBy"), rs.getString("SportType"), rs.getString("City"),
                rs.getString("GameTime"), rs.getString("GameDate"), rs.getString("GameLocation"), rs.getInt("NumberOfParticipant"));
                arr.add(g);
                
            }
        rs.close();
        conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }
    
    public ArrayList UpdateGames(int lastGameAtClient)
    {
        ArrayList<Game> arr = new ArrayList<Game>();
        Statement st;
        try {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        st = conn.createStatement();
        Statement stselect = conn.createStatement();
        ResultSet rs = stselect.executeQuery("select * from SPORTEAMGAMES where GameNumber > 'lastGameAtClient'");
        while (rs.next())
            {
                Game g = new Game(rs.getString("CreatedBy"), rs.getString("SportType"), rs.getString("City"),
                rs.getString("GameTime"), rs.getString("GameDate"), rs.getString("Location"), rs.getInt("NumberOfParticipant"));
                arr.add(g);
                rs.close();
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }
    
}
