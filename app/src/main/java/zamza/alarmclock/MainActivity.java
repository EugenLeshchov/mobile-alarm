package zamza.alarmclock;

import android.app.AlarmManager;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import zamza.alarmclock.domain.Alarm;

public class MainActivity extends AppCompatActivity {

    public static final String ALARM_ID = "zamza.alarmclock.ALARMID";

    private AppDatabase db;
    List<Alarm> alarms;
    AlarmManager alarmManager;

    public void sendMessage(View view){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "alarmdb").allowMainThreadQueries().build();
        db.alarmDao().insert(new Alarm(16, 23, "df", true));
        alarms = db.alarmDao().getAll();

        ListView lvAlarms = (ListView) findViewById(R.id.lvAlarms);
        ArrayAdapter<Alarm> alarmAdapter = new ArrayAdapter<Alarm>(this, R.layout.listview_alarm_item, alarms);
        lvAlarms.setAdapter(alarmAdapter);

        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                intent.putExtra(ALARM_ID, alarms.get(position).getId());
                startActivity(intent);
            }
        };

        lvAlarms.setOnItemClickListener(adapterViewListener);

    }
}