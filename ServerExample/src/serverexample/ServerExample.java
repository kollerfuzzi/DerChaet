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
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author Alexander Mayer
 */
public class ServerExample
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            ServerSocket server = new ServerSocket(5555);
            System.out.println("Server gestartet.");
            
            Socket client = server.accept();
            
            //Streams zum Kommunizieren
            
            OutputStream outputstream = client.getOutputStream();
            PrintWriter writer = new PrintWriter(outputstream);
            
            InputStream inputstream = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
            
            System.out.println("lele");
            
            String s = "";
            while((s = reader.readLine()) != null)
            {
                writer.write(s+"\n");
                writer.flush();
                System.out.println("Empfangen vom Client: " + s);
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
