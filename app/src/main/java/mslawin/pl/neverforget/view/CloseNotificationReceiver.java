package mslawin.pl.neverforget.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import mslawin.pl.neverforget.R;

public class CloseNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            Toast.makeText(context, R.string.cancel_notification_not_found, Toast.LENGTH_LONG).show();
        } else {
            int notificationId = Integer.parseInt(action);
            Intent newIntent = new Intent(context, AddReminderActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.putExtra(AddReminderActivity.NOTIFICATION_ID, notificationId);
            context.startActivity(newIntent);
        }
    }
}