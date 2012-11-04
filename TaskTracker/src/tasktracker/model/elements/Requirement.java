package tasktracker.model.elements;

import java.io.Serializable;

/**
 * 
 * @author jasiewsk
 *
 */
public abstract class Requirement implements Serializable {
	
	//Will add abstract content property to allow for the viewing
	//of our requirements.
	
	private static final long serialVersionUID = 1L;
	private String text;
	
	public Requirement(String text){
		
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
