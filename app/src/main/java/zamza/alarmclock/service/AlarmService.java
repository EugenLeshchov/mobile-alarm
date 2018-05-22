package zamza.alarmclock.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import zamza.alarmclock.AppDatabase;
import zamza.alarmclock.MainActivity;
import zamza.alarmclock.dao.AlarmDao;
import zamza.alarmclock.domain.Alarm;

public class AlarmService {

    private static AlarmService service;

    private AlarmDao alarmDao;
    private AlarmManager alarmManager;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private Context context;
    private Calendar calendar;

    private AlarmService(Context context){
        this.context = context;
        alarmDao = Room.databaseBuilder(context, AppDatabase.class, "alarmdb").allowMainThreadQueries().build().alarmDao();
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(context, AlarmReceiver.class);
        calendar = Calendar.getInstance();
    }

    public static AlarmService getInstance(Context context){
        if (service == null){
            service = new AlarmService(context);
        }
        return service;
    }

    public List<Alarm> getAll(){
        return alarmDao.getAll();
    };

    public Alarm getById(int id){
        return alarmDao.getById(id);
    }


    public void insert(Alarm alarm)
    {
        alarm.setId((int)alarmDao.insert(alarm));
        if (alarm.getActive()){
            alarmIntent.putExtra(RingtoneService.SONG_ID, alarm.getSongpath());
            pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            SetCalendarTime(alarm);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void update(Alarm alarm)
    {
        pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        alarmDao.update(alarm);
        if (alarm.getActive()){
            alarmIntent.putExtra(RingtoneService.SONG_ID, alarm.getSongpath());
            pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            SetCalendarTime(alarm);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void delete(Alarm alarm)
    {
        alarmDao.delete(alarm);
        pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    private void SetCalendarTime(Alarm alarm){
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);
    }

}
