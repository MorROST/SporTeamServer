/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sporteamserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TheYoni
 */
public class ServerTest {
    ServerSocket serverSocket;
    static ArrayList<NewConnectionThread> connections = new ArrayList<NewConnectionThread>();
    static DB db;
    
    //////setting up the server 
    public ServerTest(int port)
    {
        try {
            db = new DB();
            serverSocket  = new ServerSocket(port);
            System.out.println("Test Server is waiting on port 30545");
        } catch (IOException ex) {
            System.out.println("server problem" + ex.getMessage());
        }
    }
    
    //////this function waiting for a client and when a client connect it start the newonnectionThread, start him and waiting for another connection 
    public synchronized void listen()
    {
        while(true)
        {
            Socket singleSocket;
            try {
                singleSocket = serverSocket.accept();
                System.out.println("answer");
                connections.add(new NewConnectionThread(singleSocket));
                connections.get(connections.size()-1).start();
            } catch (IOException ex) {
                System.out.println("problem " + ex.getMessage());
            }
            
        } 
    }
    
    
}
