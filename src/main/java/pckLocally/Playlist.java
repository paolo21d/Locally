package pckLocally;

import java.util.ArrayList;

public class Playlist {
    private ArrayList<Song> songs;
    private String plName;
    private int songsAmount = 0;

    Playlist() {
        songs = new ArrayList<Song>();
        songsAmount = 0;
    }

    public int getSongsAmount() {
        return songsAmount;
    }

    public int getIndexOfSong(String title) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getSongName().equals(title))
                return i;
        }
        return -1;
    }

    public String getPathOfSong(int index) {
        if (index >= songs.size())
            return null;
        return songs.get(index).getSongPath();
    }

    public void addSong(Song song) {
        songs.add(song);
        songsAmount++;
    }
}
