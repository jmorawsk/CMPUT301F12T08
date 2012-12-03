package tasktracker.view;

import android.text.InputFilter;
import android.text.Spanned;

public class Filters {

	public static InputFilter specialCharFilter(){
		InputFilter filter = new InputFilter() { 
			public CharSequence filter(CharSequence source, int start, int end, 
					Spanned dest, int dstart, int dend) { 
				for (int i = start; i < end; i++) { 
					
					if ( (source.charAt(i)=='<') || (source.charAt(i)=='>')
							|| (source.charAt(i)=='{')|| (source.charAt(i)=='}')
							|| (source.charAt(i)=='"')|| (source.charAt(i)=='"')) { 
						return ""; 
					} 
					//!Character.isLetterOrDigit(source.charAt(i))
				} 
				return null; 
			} 
		};
		return filter;
	}
}
