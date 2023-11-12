package TODOPackage;

//  @Author: Spartwo

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The updated client class acts as a relay point from user inputs into the server threads
 * while validation has been moved threadside
 */

public class Client { 
    
    private static InetAddress host;
    private static final int PORT = 1984;
    private static Socket serverLink; 
    
    private static BufferedReader in;
    private static PrintWriter out;
    
    private static Scanner sc = null;
    
    public static void main(String[] args) { 
        try {
            host = InetAddress.getLocalHost();
        } catch(UnknownHostException e) {
            System.out.println("Host ID not found!");
            System.exit(1);
        }
        
        try {
            serverLink = new Socket(host,PORT);
            in = new BufferedReader(new InputStreamReader(serverLink.getInputStream()));
            out = new PrintWriter(serverLink.getOutputStream(),true);
            sc = new Scanner(System.in); 
            
            System.out.println("Press ENTER to start.");
            System.out.println(receiveMessage());
            
            while (true) { 
                messageServer(sc.nextLine());
                // displaying server reply 
                String response = receiveMessage();
                if(response.matches("TERMINATE")) {
                    closeClient();
                    break;
                }
                System.out.println(response);
            } 
            
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } // end main
    
    private static void messageServer(String message) {
        // Replace newline characters with the custom delimiter in the message
        String modifiedMessage = message.replace("\n", "|||");
        // Send message to client
        out.println(modifiedMessage);
        out.flush(); 
    } // end messageClient
    
    private static String receiveMessage() {
        String modifiedMessage;
        try {
            modifiedMessage = in.readLine();// Replace the custom delimiter with newline character
            return modifiedMessage.replace("|||", "\n");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } // end reciever
    
    private static void closeClient() {
        try {
            System.out.println("Terminating connection...");
            serverLink.close();	
            sc.close();
        } catch(IOException e) {
            System.out.println("Unable to disconnect!");
            System.exit(1);
        }
    } // end close client
} // end class
