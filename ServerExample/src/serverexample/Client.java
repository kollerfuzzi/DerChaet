/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverexample;
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
        try
        {
            Socket client = new Socket("localhost", 5555);
            System.out.println("Client go.");
            OutputStream outputstream = client.getOutputStream();
            PrintWriter writer = new PrintWriter(outputstream);
            InputStream inputstream = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
            
            Scanner scan = new Scanner(System.in);
            Thread chat = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    String message = "";
                    while(!message.equals("exit"))
                    {
                        System.out.println("Gib dei Nachricht ein jo:");
                        message = scan.next();
                        writer.write(message + "\n");
                        writer.flush();
                        System.out.println(message);
                    }
                }
            });
            chat.start();
            
            String s = "";
            
            while((s = reader.readLine()) != null)
            {
                System.out.println("Empfangen vom Server: " + s);
            }
            writer.close();
            reader.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
