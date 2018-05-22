package zamza.alarmclock.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import zamza.alarmclock.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Receiver", "Wake up!");
        Integer songId = intent.getExtras().getInt(RingtoneService.SONG_ID);
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TRAININGCOUNTDOWN");
        wl.acquire(100);
        Intent service_intent = new Intent(context, RingtoneService.class);
        service_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        service_intent.putExtra(RingtoneService.SONG_ID, songId);
        context.startService(service_intent);
        wl.release();
    }
}
