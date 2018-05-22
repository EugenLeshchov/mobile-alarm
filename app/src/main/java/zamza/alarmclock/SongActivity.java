package zamza.alarmclock;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongActivity extends AppCompatActivity {

    public static final String SONG_NAME = "zamza.alarmclock.SONGNAME";

    private HashMap<String, Integer> songs;

    ArrayList<String> adapterListSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        ListView lvSongs = (ListView) findViewById(R.id.lvSongs);

        songs = getSongsFromRawFolder();

        adapterListSongs = new ArrayList<String>(songs.keySet());

        ArrayAdapter<String> alarmAdapter = new ArrayAdapter<String>(this, R.layout.listview_alarm_item, adapterListSongs);
        lvSongs.setAdapter(alarmAdapter);

        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(SongActivity.this, AlarmActivity.class);
                intent.putExtra(SONG_NAME, songs.get(adapterListSongs.get(position)));
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
            }
        };

        lvSongs.setOnItemClickListener(adapterViewListener);
    }

    private HashMap<String, Integer> getSongsFromRawFolder(){
        HashMap<String, Integer> songs = new HashMap<>();
        Field[] fields = R.raw.class.getFields();
        for (Field field: fields) {
            try {
                songs.put(field.getName(), field.getInt(field));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return songs;
    }

}
