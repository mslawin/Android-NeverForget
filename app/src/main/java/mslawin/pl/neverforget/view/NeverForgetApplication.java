package mslawin.pl.neverforget.view;

import android.app.Application;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mslawin.pl.neverforget.R;
import mslawin.pl.neverforget.dao.DatabaseHelper;
import mslawin.pl.neverforget.entity.NotificationEntity;
import mslawin.pl.neverforget.util.NotificationUtil;

/**
 * Created by mslawin on 3/19/16.
 */
public class NeverForgetApplication extends Application {

    private static final Logger logger = Logger.getLogger(NeverForgetApplication.class.getName());

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper helper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        try {
            for (NotificationEntity notificationEntity : helper.getNotificationDao().queryForAll()) {
                NotificationUtil.registerNotification(notificationEntity.getTitle(), notificationEntity.getMessage(),
                        notificationEntity.getId(), getApplicationContext());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unable to get list of notifications from db", e);
            Toast.makeText(getApplicationContext(), getString(R.string.application_cannot_load), Toast.LENGTH_SHORT).show();
        }
    }
}