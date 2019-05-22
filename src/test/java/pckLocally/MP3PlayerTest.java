package pckLocally;

import org.junit.Test;

import static org.junit.Assert.*;

public class MP3PlayerTest {

    @org.junit.Test
    public void getPath() {
//        MP3Player player = new MP3Player();
//        assertEquals("C:/Users/paolo/Desktop/Java Start/MP3 V2/src/sample/TS22.mp3", player.getPath());
        assertNotEquals(null, MP3Player.getInstance().getPlaylists());
    }

    @Test
    public void getInstance() {
        assertNotEquals(null, MP3Player.getInstance());
    }

    @Test
    public void resetPlayer() {

    }

    @Test
    public void setController() {

    }

    @Test
    public void getPlaylists() {
    }

    @Test
    public void pickPlaylist() {
    }

    @Test
    public void play() {
    }

    @Test
    public void pause() {
    }

    @Test
    public void reload() {
    }

    @Test
    public void setPath() {
    }

    @Test
    public void getAllDuration() {
    }

    @Test
    public void getCurrentDuration() {
    }

    @Test
    public void changeTime() {
    }

    @Test
    public void getStatus() {
    }

    @Test
    public void setVolume() {
    }

    @Test
    public void parseTime() {
    }

    @Test
    public void getCurrentPlaylist() {
    }

    @Test
    public void addSongToCurrentPlaylist() {
    }

    @Test
    public void nextSong() {
    }

    @Test
    public void prevSong() {
    }

    @Test
    public void setSong() {
    }

    @Test
    public void setRepeatMode() {
    }

    @Test
    public void setTitle() {
    }

    @Test
    public void getRate() {
    }

    @Test
    public void setRate() {
    }

    @Test
    public void deleteSong() {
    }

}