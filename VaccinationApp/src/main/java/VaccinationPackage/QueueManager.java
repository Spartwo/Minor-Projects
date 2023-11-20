package VaccinationPackage;

//File IO
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//Time Control
import java.time.LocalDate;
//Queueing
import java.util.ArrayList;

/*
 * @author Spartwo
 */

public class QueueManager {
    
     private final Boolean debug = true;
     private ArrayList<Patient> patients = new ArrayList<>(); 
     
     public QueueManager() {
        loadData();
        if(patients.isEmpty() && debug) {
            populatePatients();
            for (Patient p : patients) { 
                System.out.println("Added Patient//" + p.getName() + " " + p.getSurname() + "//Age: " + p.getAge()+ "//" + p.getPEC() + "//Priiority: " + p.getPriority());
            }
            saveData();
        }
     }
    
     public void loadData() {
        //load patient data from file between runtimes
        try {
            FileInputStream fis = new FileInputStream("patients.lst");
            ObjectInputStream ois = new ObjectInputStream(fis);
            patients = (ArrayList<Patient>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void saveData() {
        //save patient data to file between runtimes
        try {
            FileOutputStream fos = new FileOutputStream("patients.lst");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(patients);
            oos.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void createPatient(String name, String surname, LocalDate DOB, Boolean PEC) {
        Patient p = new Patient(name,surname, DOB, PEC);
        //once patient is created can assign it a priority using DOB and PEC in the object
        p.setPriority(determinePriority(p));
        //add to list and sort
        sortPatient(p);
    }
    
    private void populatePatients() {
        //debug method to turn a bunch of random lists into test data
        String[] names = {"Rylee", "Arianna", "Jermaine", "Rosemary", "Griffin", "Leia", "Bryson", "Morgan", "Gaige", "Daisy", "Damarion", "Anton", "Lilliana", "Vivian", "Fisher", "Keith", "Shaylee", "Kamryn", "Adelyn", "Rohan", "Cruz", "Dakota", "Cristian", "Addison", "Rylee", "Elisha", "Trystan", "Reese", "Ean", "Aubree", "Linda", "Johnathan", "Sydney", "Lillian", "Alan", "Rory", "Ciara", "Kyla", "Selah", "Nathanial", "Skylar", "Elsie", "Skyler", "Joe", "Henry", "Shaun", "Gaige", "Braiden", "Sydnee", "Annika"};
        String[] surnames = {"Serrano", "Church", "Rocha", "Wiggins", "Garza", "Acevedo", "Lowe", "Bender", "Blackburn", "Rivera", "Newman", "Gay", "Macias", "Mcconnell", "Mcintosh", "Cruz", "Ortiz", "Knox", "Stein", "Gregory", "Duke", "Todd", "Espinoza", "Camacho", "Mcdowell", "Rubio", "Mejia", "Jennings", "Chen", "Fitzpatrick", "Brady", "Castaneda", "Sellers", "Hardy", "Kemp", "Mueller", "Richardson", "Romero", "Ross", "Hale", "Lewis", "Pratt", "Reeves", "Blair", "Dominguez", "Mcgrath", "Norris", "Downs", "Bean", "Dyer"};
        String[] dates = {"1960-01-25", "1975-06-08", "1986-02-28", "1957-09-30", "1996-03-17", "1939-03-17", "1982-08-30", "1991-04-18", "1933-02-23", "1940-01-31", "1998-11-01", "1953-01-17", "1940-03-13", "1957-11-29", "1974-04-30", "1977-12-09", "1978-01-19", "1966-05-12", "1936-12-27", "1980-06-28", "1995-02-05", "1994-09-19", "1987-12-30", "1949-05-15", "1973-04-29", "1966-07-19", "1963-07-15", "1986-05-24", "1969-05-28", "1966-03-09", "1945-06-20", "1964-07-22", "1935-04-11", "1962-11-09", "1997-01-21", "1943-11-21", "2003-04-30", "1997-06-07", "1966-01-11", "2005-04-11", "1953-02-16", "1936-10-10", "1967-11-20", "1956-12-09", "2006-04-17", "1973-11-04", "1954-01-29", "1982-09-06", "1986-04-14", "1932-05-25"};
        Boolean[] pecs = {false, false, false, false, false, true, false, false, false, true, true, false, false, false, true, true, true, true, false, false, false, false, true, false, true, true, true, true, false, true, true, false, false, true, false, false, false, false, false, false, true, true, false, false, false, false, false, false, true, false};
        
        for (int i = 0; i < names.length; i++) {
            createPatient(names[i], surnames[i], LocalDate.parse(dates[i]), pecs[i]);
        }
    }
    
    /*Sort Functions*/
    
    private void sortPatient(Patient patient) {
        //insertion sort to sort an element in the patients list based on their priority
        int index = 0;
        
        //clear element from list before sort
        //depreciated for future editing functions
        //if(patients.contains(patient)) patients.remove(patient);
            
        //if no elements then just add
        if (patients.isEmpty()) {
            index = 0;
            System.out.println("Empty List");
        } else {
            //find the final occurance of a matching or lesser priority
            for (Patient otherPatient : patients) {
                
                //if reached the end of the list
                if (patients.indexOf(otherPatient) == (patients.size() -1)) { 
                    index = patients.size(); 
                    break;
                }
                
                //compare priorities
                if (patient.getPriority() > otherPatient.getPriority()) {
                    //if higher priority place before other
                    index = patients.indexOf(otherPatient);
                    break;
                } else if (patient.getPriority() == otherPatient.getPriority()) {
                    //if equal priority compare ages
                    if (patient.getAge() >= otherPatient.getAge()) {
                        index = patients.indexOf(otherPatient);
                        break;
                    } 
                }
            }
        }
        patients.add(index, patient);
    }
    
    public void removeBatch(int targetPriority) {
        //Method to remove all patients of a specified priority level
        
        for (int i = patients.size() - 1; i >= 0; i--) {
            if (patients.get(i).getPriority() == targetPriority) {
                patients.remove(patients.get(i));   
            }
        }
        
        saveData();
    }
    
    /*Return Functions*/
    
    public int getHighestPriority() {
        if(patients.isEmpty()) { 
            return 0;
        } else {
            return patients.get(0).getPriority();
        }
    }
    
    public String getCount(int priority) {
        int x = 0;
        int y = patients.size();
        for (Patient patient : patients) {
            if(patient.getPriority() == priority) {
                x++;
            }
        }
        
        return x + "/" + y;
    }
    
    public void removePatient(int index) {
        patients.remove(patients.remove(index));
    }
    
    public Patient getPatient(int index) {
        return patients.get(index);
    }
    
    public String printList(int priority) {
        int spacer;
        String printString = "";
        //printString = "Name \t Age \t Pre-existing Conditions \n";
        for (Patient patient : patients) {
            if (patient.getPriority() == priority) {
                //spacers to align the text due to tabs acting odd
                spacer = 50 - (patient.getName().length() + patient.getSurname().length());
                String buffer = "";
                for (int i = 0; i <= spacer; i++) {
                    buffer = buffer + " ";
                }
                
                printString = printString.concat(
                    patient.getName() + " " + patient.getSurname() + buffer + "\t"
                    + patient.getAge() + "\t" 
                    + patient.getPEC() + "\n");//append to printlist
            }
        }
        return printString;
    }
    
    public String printQueue(int targetPriority) {
        int spacer;
        String printString = "";
        //printString = "Name \tPriority \n";
        for (Patient patient : patients) {
            if (patient.getPriority() == targetPriority) {
                //spacers to align the text due to tabs acting odd
                spacer = 80 - (patient.getName().length() + patient.getSurname().length());
                String buffer = "";
                for (int i = 0; i <= spacer; i++) {
                    buffer = buffer + " ";
                }
                
                printString = printString.concat(
                    patient.getName() + " " + patient.getSurname()  + buffer + "\t"
                    + patient.getPriority() + "\n");//append to printlist
            }
        }
        return printString;
    }
    
    /*Help Functions*/
    
    private int determinePriority(Patient patient) {
        //method to determine the internal vaccination priority
        int age = patient.getAge();
        
        //Hate how this looks but it's not playing friendly with switch cases
        if(age >= 90) return 10;
        else if(between(age,80,89)) return 9;
        else if(between(age,70,79)) return 8;
        else if(between(age,65,69)) return 7;
        else if(between(age,18,64) && patient.getPEC()) return 6;
        else if(between(age,55,64)) return 5;
        else if(between(age,45,54)) return 4;
        else if(between(age,30,44)) return 3;
        else if(between(age,18,29)) return 2;
        else return 1;
    }
    
    private boolean between(int age, int minInclusive, int maxInclusive) {
        return age >= minInclusive && age <= maxInclusive;
    } 
    
}
