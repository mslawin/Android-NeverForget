package mslawin.pl.neverforget.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mslawin.pl.neverforget.R;
import mslawin.pl.neverforget.dao.DatabaseHelper;
import mslawin.pl.neverforget.entity.NotificationEntity;
import mslawin.pl.neverforget.util.NotificationUtil;

public class AddReminderActivity extends AppCompatActivity {

    private static final Logger logger = Logger.getLogger(AddReminderActivity.class.getName());
    private static final int TEXT_COLOR = Color.parseColor("#452222");

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        helper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        findViewById(R.id.add_reminder_create).setOnClickListener(getCreateListener());
        int notificationToCancelId = getIntent().getIntExtra(NOTIFICATION_ID, -1);
        if (notificationToCancelId > 0) {
            createCancelPopup(notificationToCancelId);
        }
    }

    @NonNull
    private View.OnClickListener getCreateListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText titleText = (EditText) findViewById(R.id.add_reminder_title);
                    String title = titleText.getText().toString();
                    if (title.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.add_reminder_empty_title, Toast.LENGTH_LONG).show();
                        return;
                    }
                    EditText textText = (EditText) findViewById(R.id.add_reminder_text);
                    String text = textText.getText().toString();
                    NotificationEntity notificationEntity = new NotificationEntity();
                    notificationEntity.setTitle(title);
                    notificationEntity.setMessage(text);
                    helper.getNotificationDao().create(notificationEntity);

                    NotificationUtil.registerNotification(title, text, notificationEntity.getId(), getApplicationContext());
                    titleText.getText().clear();
                    textText.getText().clear();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Unable to save notification", e);
                    Toast.makeText(AddReminderActivity.this, R.string.add_reminder_db_error, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void createCancelPopup(int notificationId) {
        // TODO:
//<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
        try {
            NotificationEntity notification = helper.getNotificationDao().queryForId(notificationId);

            TextView title = new TextView(this);
            title.setText(notification.getTitle());
            title.setGravity(Gravity.CENTER);
            title.setTextColor(TEXT_COLOR);
            title.setBackgroundColor(Color.LTGRAY);
            title.setTextSize(40);
            title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 20, 0);
            title.setLayoutParams(layoutParams);

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage(notification.getMessage())
                    .setCustomTitle(title)
                    .setPositiveButton(R.string.cancel_notification_ok, getPopupConfirmListener(this, notificationId))
                    .setNegativeButton(R.string.cancel_notification_cancel, getPopupCancelListener(this))
                    .setView(R.layout.preview_notification_layout)
                    .create();

            alertDialog.show();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unable to load notification from db", e);
            Toast.makeText(getApplicationContext(), R.string.cancel_notification_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    private OnClickListener getPopupConfirmListener(final Activity activity, final int notificationId) {
        return new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(notificationId);
                try {
                    helper.getNotificationDao().deleteById(notificationId);
                    dismissPopup(dialog, activity);
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Unable to delete notification with id: " + notificationId, e);
                    Toast.makeText(activity, R.string.cancel_notification_db_error, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private OnClickListener getPopupCancelListener(final Activity activity) {
        return new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissPopup(dialog, activity);
            }
        };
    }

    private void dismissPopup(DialogInterface dialog, Activity activity) {
        dialog.dismiss();
        activity.getIntent().removeExtra(NOTIFICATION_ID);
        moveTaskToBack(true);
    }
}