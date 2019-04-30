package pckLocally;

import javafx.beans.property.SimpleStringProperty;

public class Song {
    private SimpleStringProperty songName;
    private SimpleStringProperty songTime;
    private SimpleStringProperty path;

    public Song(String name, String time){
        songName= new SimpleStringProperty(name);
        songTime= new SimpleStringProperty(time);
    }

    public String getSongName() {
        return songName.get();
    }

    public void setSongName(String songName) {
        this.songName = new SimpleStringProperty(songName);
    }

    public String getSongTime() {
        return songTime.get();
    }

    public void setSongTime(String songTime) {
        this.songTime = new SimpleStringProperty(songTime);
    }

    public String getPath() {
        return path.get();
    }

    public void setPath(String path) {
        this.path = new SimpleStringProperty(path);
    }
}
