package pckLocally;

import javafx.util.Duration;
import org.junit.Test;

import static org.junit.Assert.*;

public class MP3PlayerTest {

    @org.junit.Test
    public void getPath() {
//        MP3Player player = new MP3Player();
//        assertEquals("C:/Users/paolo/Desktop/Java Start/MP3 V2/src/sample/TS22.mp3", player.getPath());
        assertNotEquals(null, MP3Player.getInstance().getPlaylists());
    }

//    @Test
//    public void getInstance() {
//        assertNotEquals(null, MP3Player.getInstance());
//    }
//
//    @Test
//    public void resetPlayer() { //zrezygnowac z tego raczej
//
//    }
//
//    @Test
//    public void setController() {
//
//    }
//
//    @Test
//    public void getPlaylists() {
//    }
//
//    @Test
//    public void pickPlaylist() {
//
//    }
//
//    @Test
//    public void play() {
//    }
//
//    @Test
//    public void pause() {
//    }
//
//    @Test
//    public void reload() {
//    }
//
//    @Test
//    public void setPath() {
//    }
//
//    @Test
//    public void getAllDuration() {
//    }
//
//    @Test
//    public void getCurrentDuration() {
//    }
//
//    @Test
//    public void changeTime() {
//    }
//
//    @Test
//    public void getStatus() {
//    }
//
//    @Test
//    public void setVolume() {
//    }

    @Test
    public void parseTime() {
        assertEquals("1:02", MP3Player.getInstance().parseTime(new Duration(62000)));
        assertEquals("2:20", MP3Player.getInstance().parseTime(new Duration(140000)));
    }

    @Test
    public void addSongToCurrentPlaylist() {
        int amount = MP3Player.getInstance().getStatus().currentPlaylist.getSongsAmount();
        if(amount!=0){
            Song song = MP3Player.getInstance().getStatus().currentPlaylist.getSongByIndex(MP3Player.getInstance().getStatus().currentlyPlayedSongIndex);
            MP3Player.getInstance().addSongToCurrentPlaylist(song);
            assertEquals(amount, MP3Player.getInstance().getStatus().currentPlaylist.getSongsAmount());
        }
    }

    @Test
    public void nextSong() { //failure
        Playlist playlist = MP3Player.getInstance().getStatus().currentPlaylist;
        Song song = playlist.getSongByIndex((MP3Player.getInstance().getStatus().currentlyPlayedSongIndex + 1) % playlist.getSongsAmount());
        MP3Player.getInstance().nextSong();
        assertEquals(song.getSongName(), MP3Player.getInstance().getStatus().title);
        assertEquals(song.getSongPath(), MP3Player.getInstance().getStatus().path);
    }

    @Test
    public void prevSong() { //failure
        Playlist playlist = MP3Player.getInstance().getStatus().currentPlaylist;
        Song song;
        if (MP3Player.getInstance().getStatus().currentlyPlayedSongIndex == 0)
            song = playlist.getSongByIndex(playlist.getSongsAmount() - 1);
        else
            song = playlist.getSongByIndex((MP3Player.getInstance().getStatus().currentlyPlayedSongIndex + 1) % playlist.getSongsAmount());
        MP3Player.getInstance().prevSong();
        assertEquals(song.getSongName(), MP3Player.getInstance().getStatus().title);
        assertEquals(song.getSongPath(), MP3Player.getInstance().getStatus().path);
    }

    @Test
    public void setSong() { //failure
        Playlist playlist = MP3Player.getInstance().getStatus().currentPlaylist;
        Song song = playlist.getSongByIndex((MP3Player.getInstance().getStatus().currentlyPlayedSongIndex + 1) % playlist.getSongsAmount());
        MP3Player.getInstance().setSong(song.getSongName(), song.getSongPath());
        assertEquals(song.getSongName(), MP3Player.getInstance().getStatus().title);
        assertEquals(song.getSongPath(), MP3Player.getInstance().getStatus().path);
    }

    @Test
    public void deleteSong() {
        Playlist pl = MP3Player.getInstance().getCurrentPlaylist();
        int amount = pl.getSongsAmount();
        if(amount>1){
            Song song = pl.getSongByIndex(1);
            assertTrue(MP3Player.getInstance().deleteSong(song.getSongName()));
            MP3Player.getInstance().addSongToCurrentPlaylist(song);
        }
    }

//    @Test
//    public void resetAppData() {
//        MP3Player.getInstance().resetAppData();
//        assertEquals("defaultPlaylist", MP3Player.getInstance().getCurrentPlaylist().getPlName());
//        assertNotEquals(null, MP3Player.getInstance().getStatus());
//    }
}