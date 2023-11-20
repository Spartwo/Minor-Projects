package VaccinationPackage;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/*
 * @author Spartwo
 */

public class Patient implements Serializable{
    //Class used to store patient data
    private String name;
    private String surname;
    private LocalDate DOB;
    private Boolean PEC;
    private int priority = 0;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSurname() {
        return surname;
    }

    public void setName(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
    
    public int getAge() {
        //return age from current date and stored date of birth
        LocalDate CD = LocalDate.now();
        if ((DOB != null) && (CD != null)) {
            return Period.between(DOB, CD).getYears();
        } else {
            return 0;
        }
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public Boolean getPEC() {
        return PEC;
    }

    public void setPEC(Boolean PEC) {
        this.PEC = PEC;
    }

    public Patient(String name, String surname, LocalDate DOB, Boolean PEC) {
        this.name = name;
        this.surname = surname;
        this.DOB = DOB;
        this.PEC = PEC;
    }
}
