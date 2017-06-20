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
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 *
 * @author Alexander Mayer
 */
public class Server {

    private DBAccess database;
    //getLastmessages zum Client senden

    private ArrayList<ServerClient> clients = new ArrayList<>();

    public static void main(String[] args) {
        Server se = new Server();
        try {
            se.startServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startServer() throws IOException {
        System.out.println("Server started.");
        ServerSocket server = new ServerSocket(5555);
        while (true) {
            try {
                Socket client = server.accept();
                System.out.println("Client connected");
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String cred = "";
                cred = AES2Point0.decryptHyperSecurely(reader.readLine());
                String[] split = cred.split("\0");
                
                try {
                    database = new DBAccess("chaet");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                if (split.length > 1 && database.checkUserCredentials(split[0], split[1])) {
                    System.out.println("User credentials are correct");
                    ServerClient cl = new ServerClient(client, (Message t)
                            -> {
                        receiveMsg(t);
                        return null;
                    }, split[0]);
                    cl.sendMsg("LOGIN SUCCESSFUL");
                    this.clients.forEach(broadcast
                            -> broadcast.sendMsg(cl.getName() + " connected!")
                    );
                    this.clients.add(cl);
                    Thread clThread = new Thread(cl);
                    clThread.start();
                    database.getLastMessages(50).stream().map(msg -> msg.toString()).forEach(cl::sendMsg);
                } else {
                    System.out.println("Login failed");
                    PrintWriter writer = new PrintWriter(client.getOutputStream());
                    writer.write(AES2Point0.encryptHyperSecurely("LOGIN FAILED") + "\n");
                    writer.flush();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void receiveMsg(Message msg) {
        ServerClient toDisconnect = null;
        for (ServerClient client : clients) {
            if (!msg.getMessage().contains("\0exit")) {
                try {
                    client.sendMsg(msg.toString());
                    database.storeMessage(msg);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                toDisconnect
                        = clients.stream().filter(c -> c.getName().equals(msg.getUsername())).findFirst().get();
            }
        }
        if (toDisconnect != null) {
            disconnectClient(toDisconnect);
        }
    }

    public void disconnectClient(ServerClient client) {
        clients.remove(client);
        for (ServerClient c : clients) {
            c.sendMsg(client.getName() + " disconnected");
            System.out.println(client.getName() + " disconnected\n");
        }
    }
}
