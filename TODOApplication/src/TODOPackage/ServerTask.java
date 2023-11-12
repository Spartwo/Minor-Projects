package TODOPackage;

// @Author: Spartwo

import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * This class is used as data storage for the listed TODOlist tasks
 */

public class ServerTask implements Serializable {
    // Declare stored variables
    private String action;
    private Date deadline;
    private final int identifier;

    // Constructor called during initialisation to set variables using external parameters
    public ServerTask(String action, Date deadline, int identifier) {
        this.action = action;
        this.deadline = deadline;
        this.identifier = identifier;
    }
    
    // Create and return a user-readable format of the stored data
    public String getData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String deadlineString = formatter.format(deadline);
        
        String returnString = 
                "\n\t-----------------" +
                "\n\t" + deadlineString.toString() + 
                "\n\t#" + identifier + 
                "\n\t" + action;
        return returnString;
    }
    
    // Setters to directly modify existing entries
    public void setAction(String action) {
        this.action = action;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
    
    public Date getDeadline() {
        return deadline;
    }
    
    // Identifier used for search and deletion purposes
    public int getIdentifier() {
        return identifier;
    }
} // end class
