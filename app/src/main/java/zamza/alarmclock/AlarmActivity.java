package zamza.alarmclock;

import android.app.AlarmManager;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import zamza.alarmclock.domain.Alarm;
import zamza.alarmclock.service.AlarmService;

public class AlarmActivity extends AppCompatActivity {

    private Alarm alarm;
    private AlarmService alarmService;

    private static String HOUR = "HOUR";
    private static String MINUTE = "MINUTE";
    private static String ACTIVE = "ACTIVE";
    private static String ALARMID = "ALARMID";
    private final String SONGID = "SONGID";

    private TimePicker timePicker;
    private Button btnSong;
    private Switch switchActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        btnSong = (Button) findViewById(R.id.btnSong);
        switchActive = (Switch) findViewById(R.id.switchActive);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                goToMainActivity();
            }
        });

        btnSong.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                Intent intent = new Intent(AlarmActivity.this, SongActivity.class);
                intent.putExtra(HOUR, timePicker.getCurrentHour());
                intent.putExtra(MINUTE, timePicker.getCurrentMinute());
                intent.putExtra(ACTIVE, switchActive.isChecked());
                intent.putExtra(ALARMID, alarm.getId());
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                alarm.setHour(timePicker.getCurrentHour());
                alarm.setMinute(timePicker.getCurrentMinute());
                alarm.setActive(switchActive.isChecked());
                if (alarm.getSongpath() == 0){
                    btnSong.setError("Song must be chosen");
                    return;
                }
                if (alarm.getId() == 0){
                    alarmService.insert(alarm);
                } else{
                    alarmService.update(alarm);
                }
                goToMainActivity();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (alarm.getId() != 0){
                    alarmService.delete(alarm);
                }
                goToMainActivity();
            }
        });

        alarmService = AlarmService.getInstance(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (extras.containsKey(MainActivity.ALARM_ID)){
                int id = extras.getInt(MainActivity.ALARM_ID);
                alarm = getAlarm(id);
            } else if (extras.containsKey(SongActivity.SONG_NAME)){
                alarm = new Alarm(extras.getInt(HOUR), extras.getInt(MINUTE), extras.getInt(SongActivity.SONG_NAME), extras.getBoolean(ACTIVE));
                alarm.setId(extras.getInt(ALARMID));
            }
        }

        updateView();
    }

    private Alarm getAlarm(int id){
        if (id != 0){
            return alarmService.getById(id);
        } else{
            return new Alarm(12, 00, 0, true);
        }
    }

    private void updateView(){
        timePicker.setCurrentHour(alarm.getHour());
        timePicker.setCurrentMinute(alarm.getMinute());
        int songId = alarm.getSongpath();
        if (songId == 0){
            btnSong.setText("Choose song");
        } else {
            String resourceName = getResources().getResourceName(songId);
            btnSong.setText(resourceName.substring(resourceName.lastIndexOf('/')+1));
        }
        switchActive.setChecked(alarm.getActive());
    }

    private void goToMainActivity(){
        Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
        startActivity(intent);
    }

}