/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derchaetserver;

import beans.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import javafx.util.Callback;

/**
 *
 * @author Alexander Mayer
 */
public class ServerClient implements Runnable {

    private PrintWriter pw = null;
    private Socket client = null;
    private String username = null;
    private Callback<Message, Void> sendToOthers;

    public ServerClient(Socket client, Callback<Message, Void> sendToOthers, String username) throws IOException {
        this.client = client;
        this.username = username;
        this.sendToOthers = sendToOthers;
        pw = new PrintWriter(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            if (username != null) {
                String ln;
                try {
                    while ((ln = reader.readLine()) != null) {
                        String decrypted = AES2Point0.decryptHyperSecurely(ln);
                        this.sendToOthers.call(new Message(this.getName(), decrypted, LocalDateTime.now()));
                    }
                } catch (SocketException exc) {
                    System.out.println("Client disconnected");
                    sendToOthers.call(new Message(this.getName(), "\0exit", LocalDateTime.now()));
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            client.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void sendMsg(String msg) {
        pw.write(AES2Point0.encryptHyperSecurely(msg) + "\n");
        pw.flush();
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public PrintWriter getPw() {
        return pw;
    }

    public void setPw(PrintWriter pw) {
        this.pw = pw;
    }

    public String getName() {
        return username;
    }
}
