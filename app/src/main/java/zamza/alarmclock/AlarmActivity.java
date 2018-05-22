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

    private TimePicker timePicker;
    private EditText editPath;
    private Switch switchActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        editPath = (EditText) findViewById(R.id.editPath);
        switchActive = (Switch) findViewById(R.id.switchActive);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
                startActivity(intent);
                goToMainActivity();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                alarm.setHour(timePicker.getCurrentHour());
                alarm.setMinute(timePicker.getCurrentMinute());
                alarm.setActive(switchActive.isChecked());
                alarm.setSongpath(editPath.getText().toString());
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

        Integer id = getIntent().getIntExtra(MainActivity.ALARM_ID, 0);
        if (id != 0){
            alarm = alarmService.getById(id);
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

    private void goToMainActivity(){
        Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
