/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sporteamserver;

import com.yonimor.sporteam.sporteam.com.data.ConnectionData;
import com.yonimor.sporteam.sporteam.com.data.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public static PreparedStatement pstRecipes;
    
    public static final String InsertUserSQL = "INSERT into SPORTEAMUSERS values(?,?,?,?,?)";
    
    
    public DB()
    {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            pstUsers = conn.prepareStatement(InsertUserSQL);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int LogIn(String email, String password)
    {
        Statement st;
        String emailReturn;
        String passwordReturn;
        
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            st = conn.createStatement();
            Statement stselect = conn.createStatement();
            ResultSet rs = stselect.executeQuery("select * from SporTeamUsers");
            while (rs.next())
            {
                emailReturn = rs.getString("UserName");
                passwordReturn = rs.getString("Password");
                if (email.equals(emailReturn) && password.equals(passwordReturn))
                {
                    rs.close();
                    conn.close();
                    return ConnectionData.OK;
                }
            }
            
            return ConnectionData.NOT_OK;
            
        } catch (SQLException ex) {
            return ConnectionData.SOMTHING_WRONG;
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
            return ConnectionData.OK;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return ConnectionData.SOMTHING_WRONG;
        }
    }
    
}
