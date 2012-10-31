package Tasks;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private Date datecreated;
	//private int taskDeadline;
	private String description;
	private boolean requiresPhoto;
	//private int time;
	//private Status taskStatus;
	//private Visibility taskVisibility;
	//private User taskCreator;
	
	public Task(String name, Date date, String description, boolean requiresPhoto) {  
        this.setName(name);
		this.setDate(date);  
        this.setDescription(description);
        this.setRequiresPhoto(requiresPhoto);
    }
	
	public String getName(){
		
		return name;
	}
	
	public void setName(String name){
		
		this.name = name;
	}

	public Date getDate() {
		return datecreated;
	}

	public void setDate(Date date) {
		this.datecreated = date;
	}
	
	//formats the date to display properly
	public String stringDate(){
		
		
		SimpleDateFormat dateformat = new SimpleDateFormat ("yyyy-MM-dd");
		String stringdate = dateformat.format(datecreated).toString();
		System.out.println( "Test String Date: '" + stringdate + "'");
		return stringdate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	} 

	//public int getTime() {
	//	return time;
	//}

	//public void setTime(int time) {
	//	this.time = time;
	//}
	
	public boolean getRequiresPhoto(){
		
		return requiresPhoto;
		
	}
	
	public void setRequiresPhoto(boolean requiresPhoto){
		
		this.requiresPhoto = requiresPhoto;
	}
	
	//Sets the string in specific order for log entry in application.
	//public String toString() {  
		
	//	DecimalFormat  df = new DecimalFormat ("#0.00");
    //    return  this.stringDate() + " \n" + description;
//}
}