package zamza.alarmclock.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import zamza.alarmclock.MainActivity;
import zamza.alarmclock.R;

public class RingtoneService extends Service {

    public static final String NOTIFICATION = "zamza.alarmclock.services.NOTIFICATIONS";
    public static final String STOP = "zamza.alarmclock.services.STOP";
    public static final String SONG_ID = "zamza.alarmclock.SONGID";

    MediaPlayer mediaPlayer;

    private int songId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();

        if (extras != null){
          if (extras.containsKey(STOP))
          {
              mediaPlayer.stop();
              mediaPlayer.reset();
              return START_NOT_STICKY;
          } else if (extras.containsKey(RingtoneService.SONG_ID)){
              songId = extras.getInt(RingtoneService.SONG_ID);
          }
        }

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
        intent_main_activity.putExtra(NOTIFICATION, true);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent_main_activity, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Alarm notification")
                    .setContentText("Tap to disable sound")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            manager.notify(0, notification);
        }

        mediaPlayer = MediaPlayer.create(this, songId);
        mediaPlayer.start();
        return START_NOT_STICKY;
    }
}
