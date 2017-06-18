/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derchaetserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Alexander Mayer
 */
public class Client
{
    public static void main(String[] args)
    {
        Client cl = new Client();
        cl.makeClienting();
    }

    private void makeClienting()
    {
        try
        {
            Scanner scan = new Scanner(System.in);

            System.out.print("Enter your name to log in: ");
            String username = scan.nextLine();
            System.out.print("Enter your password to log in: ");
            String password = scan.nextLine();

            Socket client = new Socket("localhost", 5555);
            System.out.println("Client go.");
            OutputStream outputstream = client.getOutputStream();
            PrintWriter writer = new PrintWriter(outputstream);
            InputStream inputstream = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
            writer.write(username + "\0" + password + "\n");
            writer.flush();

            Thread chat = new Thread(() ->
            {
                String message = "";
                while (true)
                {
                    if (Thread.interrupted())
                    {
                        return;
                    }
                    System.out.println("Enter message:");
                    message = scan.nextLine();
                    writer.write(message + "\n");
                    writer.flush();
                }
            });
            chat.start();

            String s = "";

            while ((s = reader.readLine()) != null)
            {
                System.out.println("Received from server: " + s);
            }
            writer.close();
            reader.close();
            chat.interrupt();
            System.out.println("End.");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

//    private void enterNameAndPassword(Scanner scan, PrintWriter writer)
//    {
//        System.out.print("Enter your name to log in: ");
//        String username = scan.nextLine();
//        System.out.print("Enter your password to log in: ");
//        String password = scan.nextLine();
//        writer.write(username + "\0" + password);
//        writer.flush();
//    }
}
