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

public class AlarmActivity extends AppCompatActivity {

    public static final String ALARM_ID = "zamza.alarmclock.ALARMID";

    Alarm alarm;
    AppDatabase db;
    AlarmManager alarmManager;

    TimePicker timePicker;
    EditText editPath;
    Switch switchActive;
    Button btnCancel;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        editPath = (EditText) findViewById(R.id.editPath);
        switchActive = (Switch) findViewById(R.id.switchActive);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                alarm.setHour(timePicker.getCurrentHour());
                alarm.setMinute(timePicker.getCurrentMinute());
                alarm.setActive(switchActive.isChecked());
                alarm.setSongpath(editPath.getText().toString());
                if (alarm.getId() == 0){
                    db.alarmDao().insert(alarm);
                } else{
                    db.alarmDao().update(alarm);
                }
                Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "alarmdb").allowMainThreadQueries().build();
        Integer id = getIntent().getIntExtra(ALARM_ID, 0);
        if (id != 0){
            alarm = db.alarmDao().getById(id);
        } else{
            alarm = new Alarm(12, 00, ".", true);
        }
        updateView();
    }

    private void updateView(){
        timePicker.setCurrentHour(alarm.getHour());
        timePicker.setCurrentMinute(alarm.getMinute());
        editPath.setText(alarm.getSongpath());
        switchActive.setChecked(alarm.getActive());
    }
}
