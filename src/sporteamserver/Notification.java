/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sporteamserver;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author TheYoni
 */
public class Notification {
    
    public static void SendNotification(String to, String title)
    {
        try{
        URL obj = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "key=AAAAup7m7k4:APA91bHetvJdbvqbY6EJ51W8kZzFUQlhNvuRuow7W6Krku-C_6WDvrAPdEyOy55jzEV53uWTHzsRSpsRGD9MjjwY_wYOo9ffhmXFXIXBr0uWCcCiP1v38HKibxOpAd9KLPrxcdJYcCRm");
        
        JSONObject msg=new JSONObject();
        msg.put("message","test8");
        
        JSONObject notification=new JSONObject();
        //notification.put("body", "this is the body");
        notification.put("title", title);

        JSONObject parent=new JSONObject();

        parent.put("to", to);
        parent.put("notification", notification);
        parent.put("data", msg);

        con.setDoOutput(true);
        OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
        os.write(parent.toString());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println(responseCode);
        }
        catch (Exception e)
        {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
