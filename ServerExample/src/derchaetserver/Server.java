/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derchaetserver;

import beans.Message;
import database.DBAccess;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Alexander Mayer
 */
public class Server
{
    private DBAccess database;
    //getLastmessages zum Client senden

    private ArrayList<ServerClient> clients = new ArrayList<>();

    public static void main(String[] args)
    {
        Server se = new Server();
        try
        {
            se.startServer();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void startServer() throws IOException
    {
        System.out.println("Server started.");
        ServerSocket server = new ServerSocket(5555);
        while (true)
        {
            try
            {
                Socket client = server.accept();
                System.out.println("Found Client in the deepest depths of the intern net. It was a very dangerous discovery. xD.");
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String cred = "";
                cred = reader.readLine();
                String[] split = cred.split("\0");

                try
                {
                    database = new DBAccess("derchaet");
                }
                catch (ClassNotFoundException ex)
                {
                    ex.printStackTrace();
                }

                if (database.checkUserCredentials(split[0], split[1]))
                {
                    ServerClient cl = new ServerClient(client, (Message t) ->
                    {
                        receiveMsg(t);
                        return null;
                    }, split[0]);

                    this.clients.add(cl);
                    Thread clThread = new Thread(cl);
                    clThread.start();
                    database.getLastMessages(50).stream().map(msg -> msg + "\n").forEach(cl::sendMsg);
                }
                else
                {
                    System.out.println("failed");
                    client.close();
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void receiveMsg(Message msg)
    {
        clients.forEach((ServerClient c) ->
        {
            if (!msg.getMessage().equals("\0exit"))
            {
                try
                {
                    c.sendMsg(msg + "\n");
                    database.storeMessage(msg);
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            else
            {
                disconnectClient(c);
            }
        });
    }

    public void disconnectClient(ServerClient client)
    {
        clients.remove(client);
        for (ServerClient c : clients)
        {
            c.sendMsg(client.getName() + " disconnected\n");
            System.out.println(client.getName() + " disconnected\n");
        }
    }

//    public void doStuff()
//    {
//        try
//        {
//            ServerSocket server = new ServerSocket(5555);
//            System.out.println("Server gestartet.");
//            LinkedList<PrintWriter> writers = new LinkedList();
//
//            while (true)
//            {
//                Socket client = server.accept();
//                //Datenbank
//                ServerClient sc = ServerClient(client, username);
//                xD.add(sc);
//                writers.add(new PrintWriter(client.getOutputStream()));
//                Thread threadForClients = new Thread(() ->
//                {
//                    try
//                    {
//                        InputStream inputstream = client.getInputStream();
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
//
//                        String line = "";
//                        while ((line = reader.readLine()) != null)
//                        {
//                            final String jo = line;
//                            writers.forEach(toSend ->
//                            {
//                                toSend.write(jo + "\n");
//                                toSend.flush();
//                            });
//                            System.out.println("Empfangen vom Client: " + line);
//                        }
//                        reader.close();
//                    }
//                    catch (IOException ex)
//                    {
//                        ex.printStackTrace();
//                    }
//                    System.out.println("End");
//                });
//                threadForClients.start();
//            }
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
//    }
}
