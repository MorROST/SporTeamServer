/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sporteamserver;

import com.yonimor.sporteam.sporteam.com.data.ConnectionData;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TheYoni
 */
public class NewConnectionThread extends Thread{
    Socket socket;
    InputStream input;
    OutputStream output;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    
    boolean isConnected = true;
    
    public NewConnectionThread(Socket socket)
    {
            this.socket = socket;       
    }
    
    @Override
    public void run()
    {
        try{
        input = socket.getInputStream();
            output = socket.getOutputStream();
            oos = new ObjectOutputStream(output);
            ois = new ObjectInputStream(input);
            ////the result is an object so no metter what the DB returns it will be cast as needed
            Object result;
                
            ////loop to keep listenening to the client
            while(isConnected)
            {
                //////waiting for the client request  (response at the end of the switch and case)
                ConnectionData requestCD = (ConnectionData)ois.readObject();
                ConnectionData responseCD = new ConnectionData();
                switch(requestCD.getRequestCode())
                {
                    case ConnectionData.LOGIN:
                    {
                        result = (ServerTest.db.LogIn(requestCD.getEmail(), requestCD.getPassword()));
                        responseCD.setWorked((int)result);
                        break;
                    }
                    case ConnectionData.REGISTER:
                    {
                        result = (ServerTest.db.Register(requestCD.getUser()));
                        responseCD.setWorked((int)result);
                        break;
                    }
                }
                /////////return the response to the client
                oos.writeObject(responseCD);
               
            }
                
         }catch (IOException ex) {
            //Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("out");
             isConnected=false;
             
        }catch(ClassNotFoundException e)
        {
            System.out.println("Class not found");
        }
        ///////////remove the connection from the connctions arraylist
        finally
         {
             if(socket != null)
                 try {
                     System.out.println("closing socket");
                     for(NewConnectionThread nCT:ServerTest.connections)
                     {
                         if (nCT.socket == this.socket)
                         {
                             ServerTest.connections.remove(nCT);
                             break;
                         }
                     }
                     System.out.println(this.socket.toString() + " closed");
                     socket.close();
             } catch (IOException ex) {
                     System.out.println("outtt");
             }
         }
    }
}
