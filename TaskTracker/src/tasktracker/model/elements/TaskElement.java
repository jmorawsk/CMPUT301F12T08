package tasktracker.model.elements;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import tasktracker.*;

/**
 * 
 * @author Katherine Jasniewski
 * Edits: Jeanine Bonot
 *
 */
public class TaskElement implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// Used to generate the task ID number.
	private static int _taskCount = 0;
	
	private String name;
	private String description;
	private int id;
	private Date creationDate;
	private List<Requirement> requirements;
	private User creator;
	
	//private int time;
	//private Status taskStatus;
	//private Visibility taskVisibility;
	//private User taskCreator;
	//private int taskDeadline;
	
	public TaskElement(User creator, Date date){
		this(creator, "Untitled", date, "", new ArrayList<Requirement>());
	}
	
	public TaskElement(User creator, String name, Date date, String description, List<Requirement> arrayList) {  
        
		// Required Elements
		this.creator = creator;
        this.creationDate = date;
        this.id = ++_taskCount;
        
        // Optional Elements
		this.name = name;
        this.description = description;
        this.requirements = arrayList;
        
    }
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public Date getDate() {
		return creationDate;
	}
	
	public int getID(){
		return this.id;
	}

	public String stringDate(){

		SimpleDateFormat dateformat = new SimpleDateFormat ("yyyy-MM-dd");
		String stringdate = dateformat.format(creationDate).toString();
		System.out.println( "Test String Date: '" + stringdate + "'");
		return stringdate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Requirement> getRequirements() {
		return requirements;
	}

	public void setRequirements(ArrayList<Requirement> requirements) {
		this.requirements = requirements;
	}
	
	public void deleteRequirement(int requirement){
		
		requirements.remove(requirement);
		
	}
	
	public void editRequirement(int requirement, String newText){
		
		requirements.get(requirement).setText(newText);
		
	}
	
	//Sets the string in specific order for log entry in application.
	//public String toString() {  
		
	//	DecimalFormat  df = new DecimalFormat ("#0.00");
    //    return  this.stringDate() + " \n" + description;
//}
}