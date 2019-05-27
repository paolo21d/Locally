package pckLocally;

import org.junit.Test;

import static org.junit.Assert.*;

public class AllPlaylistsTest {

    @Test
    public void getPlaylistByName() {
        AllPlaylists pl = new AllPlaylists();
        assertEquals("defaultPlaylist", pl.getPlaylistByName("defaultPlaylist").getPlName());
    }

    @Test
    public void getAllPlaylists() {
        AllPlaylists pl = new AllPlaylists();
        assertEquals("defaultPlaylist", pl.getAllPlaylists().get(0).getPlName());
        assertEquals(0, pl.getAllPlaylists().get(0).getSongsAmount());
        assertEquals(0, pl.getAllPlaylists().get(0).getAllSongs().size());
    }

    @Test
    public void readPlaylistsFromFile() {
        AllPlaylists pl = new AllPlaylists();
        pl.readPlaylistsFromFile("testRead.json");
        assertEquals(2, pl.getPlaylistsAmount());
        assertNotNull(pl.getAllPlaylists());
        assertNotNull(pl.getPlaylistByName("defaultPlaylist"));
        assertNotNull(pl.getPlaylistByName("NewPlaylist"));
        assertEquals("defaultPlaylist", pl.getPlaylistByName("defaultPlaylist").getPlName());
    }

    @Test
    public void writePlaylistsToFile() {
        AllPlaylists pl = new AllPlaylists();
        assertEquals("defaultPlaylist", pl.getPlaylistByName("defaultPlaylist").getPlName());
        assertEquals(0, pl.getPlaylistByName("defaultPlaylist").getSongsAmount());
        assertEquals(1, pl.getPlaylistsAmount());

        pl.writePlaylistsToFile("testWrite.json");
    }


}