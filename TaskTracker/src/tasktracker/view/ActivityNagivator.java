package tasktracker.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * An listener that navigates an activity to another when a view is clicked.
 * 
 * @author Jeanine Bonot
 *
 */
public class ActivityNagivator implements OnClickListener {
	Context applicationContext;
	Class<? extends Activity> destination;
	
	public ActivityNagivator(Context context, Class<? extends Activity> destination){
		this.applicationContext = context;
		this.destination = destination;
	}

	public void onClick(View v) {
		Intent intent = new Intent(this.applicationContext, this.destination);
		applicationContext.startActivity(intent);
	}
	
}
