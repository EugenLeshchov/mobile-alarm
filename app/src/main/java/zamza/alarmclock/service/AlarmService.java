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

import java.util.List;

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

    private AlarmService(Context context){
        alarmDao = Room.databaseBuilder(context, AppDatabase.class, "alarmdb").allowMainThreadQueries().build().alarmDao();
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(context, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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


    public void insert(Alarm alarm){
        alarmDao.insert(alarm);
    }

    public void update(Alarm alarm){
        alarmDao.update(alarm);
    }

    public void delete(Alarm alarm){
        alarmDao.delete(alarm);
    }

}
