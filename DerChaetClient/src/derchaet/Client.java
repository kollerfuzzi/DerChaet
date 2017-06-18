/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derchaet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author Alexander Mayer
 */
public class Client {

    private String username;
    private String server;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private MessageContainer cont;

    public Client(MessageContainer cont) {
        this.cont = cont;
    }

    /**
     * Connects to the Server
     *
     * @param username
     * @param password
     * @param server
     * @throws java.io.IOException
     */
    public void connect(String username, String password, String server) throws IOException, Exception {
        this.username = username;
        this.server = server;

        socket = new Socket(this.server, 5555);
        OutputStream outputstream = socket.getOutputStream();
        writer = new PrintWriter(outputstream);
        sendMsg(this.username + "\0" + password);
        
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String succ = AES2Point0.decryptHyperSecurely(reader.readLine());
        if(succ.equals("LOGIN FAILED")) {
            throw new Exception("Login Failed!");
        }

        Task receiveTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                receive();
                return null;
            }
        };
        new Thread(receiveTask).start();
        
        System.out.println("Client initialized");
    }

    public void receive() throws IOException {
        System.out.println("I'm Receiving !");
        String s = "";
        
        while ((s = reader.readLine()) != null) {
            String decrypted = AES2Point0.decryptHyperSecurely(s);
            System.out.println("Received from server: " + decrypted);
            final MessageContainer cont = this.cont;
            final String message = decrypted;
            Platform.runLater(() -> {
                cont.append(message);
            });
        }
        reader.close();
    }
    
    public void disconnect() {
        sendMsg("\0exit");
    }

    public void sendMsg(String msg) {
        System.out.println("Sent message: " + msg);
        writer.write(AES2Point0.encryptHyperSecurely(msg) + "\n");
        writer.flush();
    }

    public String getUsername() {
        return username;
    }

    public String getServer() {
        return server;
    }

}
