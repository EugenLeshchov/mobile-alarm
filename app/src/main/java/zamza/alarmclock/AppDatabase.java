package zamza.alarmclock;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import zamza.alarmclock.dao.AlarmDao;
import zamza.alarmclock.domain.Alarm;

@Database(entities = {Alarm.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AlarmDao alarmDao();
}
