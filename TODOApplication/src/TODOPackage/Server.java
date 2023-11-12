package TODOPackage;

//  @Author: Spartwo

import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * This class is the main reciever server which instantiates threads 
 * and is responsible for saving and loading the task list between runtimes 
 */

public class Server { 
    // Declare server variables
    private static ServerSocket server = null;
    private static final int PORT = 1984;
    private static int activeConnections = 0;
    private static int connections = 0;
    // Declare an ArrayList of custom objects (e.g., MyObject)
    private static ArrayList<ServerTask> taskList;
    
    public static void main(String[] args) 
    { 
        // Load the internal tasklist before intialising the server
        loadTasks();
        try { 
            server = new ServerSocket(PORT); 
            server.setReuseAddress(true); 
            System.out.println("Server is listening on port " + PORT);
            while (true) { 
                // socket object to receive incoming client 
                Socket clientSocket;
                try {
                    clientSocket = server.accept();
                    changeConnections(true);
                    // Declare and start thread
                    ServerThreadRun resource = new ServerThreadRun(clientSocket, connections);
                    new Thread(resource).start();
                } catch (IOException e) {
                    System.out.println("UNABLE TO CREATE THREAD");
                    e.printStackTrace();
                }
            } 
        } catch (IOException e) {
        System.out.println("UNABLE TO ATTACH TO PORT");
        }
    } // end main
    
    // Change the number of active connections for management
    public static void changeConnections(boolean increase) {
        if (increase) { 
            connections++;
            activeConnections++;
        } else {
            activeConnections--;
        }
        System.out.println("NEW THREADCOUNT: " + activeConnections);
    } // end Change Connections
    
    public static int getConnectionCount() {
        return connections;
    } // end Get Connection Count
    
    public static ArrayList<ServerTask> getTaskList() {
        return (ArrayList<ServerTask>) taskList.clone();
    } // end get task list
    
    public static void addTask(ServerTask task) {
        taskList.add(task);
    }
    
    public static void removeTask(int index) {
        taskList.remove(index);
    }
    
    public static void updateTask(int index, String action, Date deadline) {
        ServerTask taskToUpdate = taskList.get(index);
    
        taskToUpdate.setAction(action);
        taskToUpdate.setDeadline(deadline);
    }
            
    public static void saveTasks() {
        // Serialise and export the tasks list to a file to keep between runtimes
        try {
            FileOutputStream fos = new FileOutputStream("tasks.tdo");
            ObjectOutputStream oos = new ObjectOutputStream(fos);   
            oos.writeObject(taskList);
            oos.close(); 
            System.out.println("TASKS SAVED");
        } catch (IOException e) {
            System.out.println(e);
        } 
    } // end Save Tasks
            
    private static void loadTasks() { 
        // Define file to check
        File file = new File("tasks.tdo");
        // Check for extant data file and create a blank instance if not found
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream("tasks.tdo");
                ObjectInputStream ois = new ObjectInputStream(fis); 
                taskList = (ArrayList<ServerTask>) ois.readObject();
                System.out.println("TASKS LOADED");
                ois.close(); 
            } catch(Exception e) {
                System.out.println(e);
            }
        } else {
            taskList = new ArrayList<ServerTask>();
            System.out.println("FILE NOT FOUND. NEW FILE CREATED");
        }
    } // end Load Tasks
}