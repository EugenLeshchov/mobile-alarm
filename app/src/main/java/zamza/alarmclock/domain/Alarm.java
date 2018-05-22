package zamza.alarmclock.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Alarm {

    public Alarm(int hour, int minute, String songpath, Boolean active){
        this.hour = hour;
        this.minute = minute;
        this.songpath = songpath;
        this.active = active;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Integer hour;

    private Integer minute;

    private String songpath;

    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getSongpath() {
        return songpath;
    }

    public void setSongpath(String songpath) {
        this.songpath = songpath;
    }

    @Override
    public String toString(){
        return String.format("%02d:%02d", hour, minute);
    }

}
