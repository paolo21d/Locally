package pckLocally;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlaylistTest { //PASSED

    @Test
    public void addSong() {
        Playlist pl = new Playlist("p");
        assertEquals(0, pl.getSongsAmount());
        pl.addSong(new Song("s1", "-", "p1"));
        assertEquals(1, pl.getSongsAmount());
        pl.addSong(new Song("s2", "-", "p2"));
        assertEquals(2, pl.getSongsAmount());
        pl.addSong(new Song("s3", "-", "p3"));
        assertEquals(3, pl.getSongsAmount());
    }

    @Test
    public void getPlName() {
        Playlist pl = new Playlist("playlist");
        assertEquals("playlist", pl.getPlName());

        pl.setPlName("playlist2");
        assertEquals("playlist2", pl.getPlName());
    }

    @Test
    public void getSongsAmount() {
        Playlist pl = new Playlist();
        assertEquals(0, pl.getSongsAmount());
    }

    @Test
    public void getIndexOfSong() {
        Playlist pl = new Playlist();
        assertEquals(-1, pl.getIndexOfSong("a"));
        pl.addSong(new Song("s1", "-", "p1"));
        pl.addSong(new Song("s2", "-", "p2"));
        pl.addSong(new Song("s3", "-", "p3"));
        assertEquals(-1, pl.getIndexOfSong("s0"));
        assertEquals(0, pl.getIndexOfSong("s1"));
        assertEquals(1, pl.getIndexOfSong("s2"));
        assertEquals(2, pl.getIndexOfSong("s3"));
    }

    @Test
    public void getPathOfSong() {
        Playlist pl = new Playlist();
        assertNull(pl.getPathOfSong(1));
        assertNull(pl.getPathOfSong(-1));
        pl.addSong(new Song("s1", "-", "p1"));
        pl.addSong(new Song("s2", "-", "p2"));
        pl.addSong(new Song("s3", "-", "p3"));
        pl.addSong(new Song("s4", "-", "p4"));
        assertEquals("p1", pl.getPathOfSong(0));
        assertEquals("p2", pl.getPathOfSong(1));
        assertEquals("p3", pl.getPathOfSong(2));
        assertEquals("p4", pl.getPathOfSong(3));
        assertNull(pl.getPathOfSong(4));
    }

    @Test
    public void getSongByIndex() {
        Playlist pl = new Playlist();
        assertNull(pl.getPathOfSong(1));
        assertNull(pl.getPathOfSong(-1));
        Song s1 = new Song("s1", "-", "p1");
        Song s2 = new Song("s2", "-", "p2");
        Song s3 = new Song("s3", "-", "p3");
        Song s4 = new Song("s4", "-", "p4");
        pl.addSong(s1);
        pl.addSong(s2);
        pl.addSong(s3);
        pl.addSong(s4);
        assertEquals(s1, pl.getSongByIndex(0));
        assertEquals(s2, pl.getSongByIndex(1));
        assertEquals(s3, pl.getSongByIndex(2));
        assertEquals(s4, pl.getSongByIndex(3));
        assertNull(pl.getPathOfSong(4));
    }

    @Test
    public void getAllSongs() {
        Playlist pl = new Playlist();
        assertNotEquals(null, pl.getAllSongs());
        Song s1 = new Song("s1", "-", "p1");
        Song s2 = new Song("s2", "-", "p2");
        Song s3 = new Song("s3", "-", "p3");
        Song s4 = new Song("s4", "-", "p4");
        pl.addSong(s1);
        pl.addSong(s2);
        pl.addSong(s3);
        pl.addSong(s4);
        ArrayList<Song> songs = pl.getAllSongs();
        assertEquals(s1,songs.get(0));
        assertEquals(s2,songs.get(1));
        assertEquals(s3,songs.get(2));
        assertEquals(s4,songs.get(3));
    }

    @Test
    public void deleteSongByTitle() {
        Playlist pl = new Playlist();
        assertFalse(pl.deleteSongByTitle("a"));
        pl.addSong(new Song("s1", "-", "p1"));
        pl.addSong(new Song("s2", "-", "p2"));
        pl.addSong(new Song("s3", "-", "p3"));
        pl.addSong(new Song("s4", "-", "p4"));
        assertFalse(pl.deleteSongByTitle("a"));
        assertTrue(pl.deleteSongByTitle("s1"));
        assertEquals(3, pl.getSongsAmount());
    }
}