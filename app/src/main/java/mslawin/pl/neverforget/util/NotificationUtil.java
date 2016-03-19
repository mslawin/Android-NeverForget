package mslawin.pl.neverforget.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import mslawin.pl.neverforget.R;
import mslawin.pl.neverforget.view.CloseNotificationReceiver;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by mslawin on 3/19/16.
 */
public final class NotificationUtil {

    public static final int TEXT_LENGTH = 27;

    public static void registerNotification(String title, String text, Integer notificationId, Context context) {
        Intent resultIntent = new Intent(context, CloseNotificationReceiver.class);
        resultIntent.setClassName(context, CloseNotificationReceiver.class.getName());
        resultIntent.setAction(notificationId.toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, resultIntent, 0);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.alert_icon)
                .setContentTitle(title)
                .setAutoCancel(false)
                .setOngoing(true)
                .setTicker(context.getString(R.string.add_reminder_ticker))
                .setContentIntent(pendingIntent);

        if (text.length() > TEXT_LENGTH) {
            nBuilder.setSubText(text.substring(TEXT_LENGTH));
            text = text.substring(0, TEXT_LENGTH);
        }
        nBuilder.setContentText(text);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, nBuilder.build());
    }
}