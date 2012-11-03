package tasktracker.view;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NotificationListView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_notification_list_view, menu);
        return true;
    }
}
