package zamza.alarmclock;

import android.app.AlarmManager;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import zamza.alarmclock.dao.AlarmDao;
import zamza.alarmclock.domain.Alarm;

public class MainActivity extends AppCompatActivity {

    public static final String ALARM_ID = "zamza.alarmclock.ALARMID";

    private AlarmDao alarmDao;
    List<Alarm> alarms;
    AlarmManager alarmManager;
    ListView lvAlarms;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmDao = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "alarmdb").allowMainThreadQueries().build().alarmDao();

        lvAlarms = (ListView) findViewById(R.id.lvAlarms);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        alarms = alarmDao.getAll();
        ArrayAdapter<Alarm> alarmAdapter = new ArrayAdapter<Alarm>(this, R.layout.listview_alarm_item, alarms);
        lvAlarms.setAdapter(alarmAdapter);
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                intent.putExtra(ALARM_ID, alarms.get(position).getId());
                startActivity(intent);
            }
        };

        btnCreate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                intent.putExtra(ALARM_ID, 0);
                startActivity(intent);
            }
        });

        lvAlarms.setOnItemClickListener(adapterViewListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        alarms = alarmDao.getAll();
    }
}