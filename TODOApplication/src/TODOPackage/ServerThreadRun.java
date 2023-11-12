package TODOPackage;

//  @Author: Spartwo

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import TODOPackage.Server;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class is a thread that is instantiated whenever a client connects to the server
 * This thread conducts most taks until manual closure independent of the server
 */

public class ServerThreadRun implements Runnable { 
    private final Socket clientLink; 
    private int threadID;
    
    private BufferedReader in = null;
    private PrintWriter out = null;

    // Constructor 
    public ServerThreadRun(Socket socket, int ID) 
    { 
        this.clientLink = socket; 
        this.threadID = ID;
    } 

    public void run() 
    { 
        try { 
            in = new BufferedReader(new InputStreamReader(clientLink.getInputStream()));
            out = new PrintWriter(clientLink.getOutputStream(),true);
 
            messageClient("====================================\nMenu"
                + "\nADD\t  Insert a new todo task"
                + "\nREMOVE\t  Delete an existing todo task"
                + "\nEDIT\t  Edit an existing todo task"
                + "\nLIST\t  List todo list tasks"
                + "\nSTOP\t  Exit the application"
                + "\n====================================\nType keyword to select");
            // Keep on looping until the link is closed
            while(!clientLink.isClosed()) { 
                menu();
            } 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return;
    } 
    
    
    private void menu() {
        // Wait for user keyword entry
        String choice = receiveMessage().toUpperCase();
        try {
            // Keyword (if present) from the message and start related actions
            switch (choice) {
            case "ADD":
                addTask();
                break;
            case "REMOVE":
                removeTask();
                break;
            case "EDIT":
                editTask();
                break;
            case "LIST":
                listTasks();
                break;
            case "STOP":
                closePort();
                break;
            default:
                throw new IncorrectActionException(choice);
            }
        } catch (IncorrectActionException e) {
            System.out.println(e.getIncorrectActionMessage());
            messageClient(e.getIncorrectActionMessage());
        }
    } // end menu
    
    private void addTask() {
        // Add a specified task to the todo list
        
        // Request the user inputs a date for the task
        Date newDate = inputDate();
        
        // Request the user inputs the action 
        messageClient("Enter the todo action:");
        String newAction = receiveMessage();
        
        // Get the current highest identifier value
        int highestIdentifier = 0; // Initialize with lowest possible
        ArrayList<ServerTask> taskList = Server.getTaskList();
        for (int i = 0; i < taskList.size(); i++) {
            ServerTask task = taskList.get(i);
            int currentIdentifier = task.getIdentifier();
            if (currentIdentifier > highestIdentifier) {
                highestIdentifier = currentIdentifier;
            }
        }
        
        // Create a new task object and add it to the list
        ServerTask task = new ServerTask(newAction, newDate, highestIdentifier + 1);
        Server.addTask(task);
        
        // Send confirmation to the client 
        messageClient("Task has been added to the todo list!");
        System.out.println("TASK ADDED");
    } // end add
    
    private void removeTask() {
        messageClient("Enter the target task ID:");
        // Make sure the search term is an int but value less important
        int targetID = getIntInput();
        ArrayList<ServerTask> taskList = Server.getTaskList();
        
        // Iterate through task objects in the ArrayList to find the task with a matching identifier.
        for (ServerTask task : taskList) {
            if (task.getIdentifier() == targetID) {
                // Get confirmation from client
                messageClient("Type YES to confirm deletion of task #" + targetID);
                if(receiveMessage().toUpperCase().matches("YES")) {
                    // Pass index point to the server for root deletion
                    Server.removeTask(taskList.indexOf(task));
                    messageClient("Task was successfuly removed from the todo list.");
                    System.out.println("TASK REMOVED SUCCESSFULLY");
                }
                return;
            }
        }
        // If no matching task is found don't worry about it
        messageClient("Task #" + targetID + " could not be found.");
        System.out.println("TASK NOT FOUND");
    } // end remove task
    
    private void editTask() {
        messageClient("Enter the target task ID:");
        // Make sure the search term is an int but value less important
        int targetID = getIntInput();
        ArrayList<ServerTask> taskList = Server.getTaskList();
        
        // Iterate through task objects in the ArrayList to find the task with a matching identifier.
        for (int i = 0; i < taskList.size(); i++) {
            ServerTask task = taskList.get(i);
            if (task.getIdentifier() == targetID) {
                messageClient("Are you changing the DATE, ACTION, or BOTH?");
                String response = receiveMessage().toUpperCase(); 
                
                // Use keyword (if present) from the message and start related actions
                try {
                    switch (response) {
                    case "DATE":
                        task.setDeadline(inputDate()); 
                        break;
                    case "ACTION":
                        messageClient("Enter the todo action:");
                        task.setAction(receiveMessage());
                        break;
                    case "BOTH":
                        task.setDeadline(inputDate()); 
                        messageClient("Enter the todo action:");
                        task.setAction(receiveMessage());
                        break;
                    default:
                        throw new IncorrectActionException(response);
                    }
                    messageClient("Task #" + targetID + " updated!");
                    System.out.println("TASK UPDATED");
                } catch (IncorrectActionException e) {
                    System.out.println(e.getIncorrectActionMessage());
                    messageClient(e.getIncorrectActionMessage());
                }
                break; // Exit the loop after successful edit.
            }
            // If no matching task is found don't worry about it
            messageClient("Task #" + targetID + " could not be found.");
            System.out.println("TASK NOT FOUND");
        }
    } // end edit task
    
    private void listTasks() {
        // Return a printable version of the todo list for a specified date or all dates
        messageClient("Type YES to see all tasks.");
        String response = receiveMessage().toUpperCase();
        
        ArrayList<ServerTask> taskList = Server.getTaskList();
        
        // Declare print stringbuilder
        StringBuilder output = new StringBuilder();
        
        output.append("====================================");
        if(taskList != null) {    
            if(response.matches("YES")) {
                // Header for all todos ever
                output.append("\n\tAll Tasks");
                
                // Iterate through the task list and append all tasks
                for (int i = 0; i < taskList.size(); i++) {
                    ServerTask task = taskList.get(i);
                    output.append(task.getData());
                }   
            } else {
                // Convert date
                Date parsedDate = inputDate();  
                Instant instant1 = parsedDate.toInstant().truncatedTo(ChronoUnit.DAYS);
                Instant instant2 = null;
                // Header for narrow date set
                // Format the date without the time component
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                output.append("\n\tTasks for " + formatter.format(parsedDate));
                    
                // Iterate through the task list and append each task during that date to the output.
                for (int i = 0; i < taskList.size(); i++) {
                    ServerTask task = taskList.get(i);
                    instant2 = task.getDeadline().toInstant().truncatedTo(ChronoUnit.DAYS);
                    if(instant2.equals(instant1)) {
                        output.append(task.getData());
                    }
                }   
            }
        }
        output.append("\n====================================");
            
        // Send the list to the client
        messageClient(output.toString());
    } // end list
    
    
    //
    //Date tomfoolery helpers 
    //
    
    private Date inputDate() {
        // Get user input for date, month, and year
        messageClient("Using numbers only; enter the date of the todo task:\nEnter the year:");
        int year = validateRange(getIntInput(), 0, Integer.MAX_VALUE);

        messageClient("Enter the month:");
        int month = validateRange(getIntInput(), 1, 12);
        
        messageClient("Enter the date:");
        int day = validateRange(getIntInput(), 1, daysInMonth(year, month));

        return createDate(year, month, day);
    } // end date input
    
    private int daysInMonth(int year, int month) {
        // Get the number of days in the calendar month
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Month is zero-based in Calendar

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    } // end days in month
    
    private Date createDate(int year, int month, int day) {
       // using the validated YMD to create a storable DATE
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Month is zero-based in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, day);

        try {
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    } //end createDate
    
    //
    //Input
    //
    private int getIntInput() {
        int input;
        while (true) {
            String userInputString = receiveMessage();
            
            try {
                input = Integer.parseInt(userInputString);
                return input;
            } catch (NumberFormatException e) {
                messageClient("Invalid input. Please enter an integer.");
            }
        }
    }

    private int validateRange(int input, int min, int max) {
        // Check user value for range accuracy
        while (input < min || input > max) {
            messageClient("Invalid input. Please enter a number between " + min + " and " + max + ".");
            input = getIntInput();
        }
        return input;
    }
    
    private void closePort() {
        // Attempt to save static tasklist
        Server.saveTasks();
        // Send user confirmation, must be done under assumption of success
        messageClient("TERMINATE");
        
        try {
            // Close connection
            clientLink.close();
            // Reduce number of connections
            Server.changeConnections(false);
        } catch (IOException ex) {
            Logger.getLogger(ServerThreadRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // end close
    
    //
    //Messengers
    //
   
    private void messageClient(String message) {
        // Replace newline characters with the custom delimiter in the message
        String modifiedMessage = message.replace("\n", "|||");
        // Send message to client
        out.println(modifiedMessage);
        out.flush(); 
    } // end messageClient
    
    private String receiveMessage() {
        try {
            String modifiedMessage = in.readLine();
            // Replace the custom delimiter with newline character
            return modifiedMessage.replace("|||", "\n");
        } catch (IOException ex) {
            Logger.getLogger(ServerThreadRun.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } // end reciever
} // end class
