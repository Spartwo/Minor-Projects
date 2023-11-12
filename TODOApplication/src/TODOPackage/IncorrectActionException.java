package TODOPackage;

//  @Author: Spartwo

/*
 * This class is applied to switch based set where the default case is not desirable.
 */

public class IncorrectActionException extends Exception {
    private String customMessage;
    
    // Zero Argument Contstructor
    public IncorrectActionException() {
        super("Invalid Action!");
        customMessage = "Invalid Action!";
    }
    
    // Argument Constructor
    public IncorrectActionException(String message)
    {
        super(message);
        customMessage = message + " is not a valid selection";
    }
    
    // Method to get the custom message
    public String getIncorrectActionMessage() {
        return customMessage;
    }
} // end class
