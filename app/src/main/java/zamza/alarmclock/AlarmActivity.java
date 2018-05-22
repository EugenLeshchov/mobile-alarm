package zamza.alarmclock;

import android.app.AlarmManager;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import zamza.alarmclock.domain.Alarm;

public class AlarmActivity extends AppCompatActivity {

    public static final String ALARM_ID = "zamza.alarmclock.ALARMID";

    private AppDatabase db;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        TextView textView = (TextView) findViewById(R.id.textView);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "alarmdb").allowMainThreadQueries().build();
        Integer id = getIntent().getIntExtra(ALARM_ID, 0);

        textView.setText(db.alarmDao().getById(id).toString());

    }
}
