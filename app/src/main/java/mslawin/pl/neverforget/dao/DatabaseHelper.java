package mslawin.pl.neverforget.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mslawin.pl.neverforget.entity.NotificationEntity;

/**
 * Created by mslawin on 3/6/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "neverforget.db";
    private static final int DATABASE_VERSION = 1;
    private static final Logger logger = Logger.getLogger(DatabaseHelper.class.getName());

    private final Dao<NotificationEntity, Integer> notificationDao;

    public DatabaseHelper(Context context) throws SQLException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        notificationDao = getDao(NotificationEntity.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NotificationEntity.class);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unable to create notification table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<NotificationEntity, Integer> getNotificationDao() {
        return notificationDao;
    }
}