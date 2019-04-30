package pckLocally;

import java.util.ArrayList;

public class Playlist {
    private ArrayList<Song> songs;
    private String plName;
    private  int songsAmount=0;

    Playlist(){
        songs = new ArrayList<Song>();
        songsAmount =0;
    }

    public int getSongsAmount() {
        return songsAmount;
    }


    public void addSong(Song song){
        songs.add(song);
        songsAmount++;
    }
}
