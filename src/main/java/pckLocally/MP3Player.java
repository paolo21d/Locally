package pckLocally;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MP3Player {
    private static MP3Player instance = null;
    private Controller controller;
    private Media media;
    private MediaPlayer mediaPlayer;
    private AllPlaylists playlists = new AllPlaylists();
    private MediaPlayer localMediaPlayer;
    private PlayerStatus status = new PlayerStatus();


    private MP3Player() {
        //////status.path = "C:/Users/paolo/Desktop/Java Start/MP3 V2/src/sample/TS22.mp3";
        //controller = c;
        playlists.readPlaylistsFromFile("");
        status.currentPlaylist = playlists.getPlaylistByName("defaultPlaylist");
    }

//    private MP3Player() {
//    }

    public static MP3Player getInstance() {
        if (instance == null) {
            instance = new MP3Player();
        }
        return instance;
    }

    public static void resetPlayer() {
        instance = null;
    }

    public void setController(Controller c) {
        controller = c;
    }

    public AllPlaylists getPlaylists() {
        return playlists;
    }

    public boolean pickPlaylist(String name) {
        mediaPlayer = null;
        status.played = false;
        status.paused = false;
        status.currentPlaylist = playlists.getPlaylistByName(name);
        status.currentlyPlayedSongIndex = 0;
        if (status.currentPlaylist.getSongsAmount() != 0) {
            status.path = status.currentPlaylist.getPathOfSong(0);
        } else
            status.path = null;
        return true;
    }

    boolean play() {
        if (status.path == null) {
            System.out.println("Path is null");
            return false;
        }
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            System.out.println(status);
            mediaPlayer.pause();
        }

        String file = new File(status.path).toURI().toString();
        if (file == null)
            return false;
        media = new Media(file);
        if (media == null)
            return false;
        mediaPlayer = new MediaPlayer(media);
        if (mediaPlayer == null)
            return false;
        mediaPlayer.play();
        mediaPlayer.setRate(status.rate);
        status.played = true;
        status.paused = false;


        mediaPlayer.setOnReady(new Runnable() {
            public void run() {
                status.allDuration = mediaPlayer.getMedia().getDuration();
                System.out.println("Czas trwania " + parseTime(status.allDuration));
                status.currentPlaylist.getSongByIndex(status.currentlyPlayedSongIndex).setSongTime(parseTime(status.allDuration)); //TODO ze zdalnego sterowania cos null
                playlists.writePlaylistsToFile("");
            }
        });

        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                controller.updateValuesTime();
                if (getAllDuration().toMillis() - getCurrentDuration().toMillis() <= 500) { //koniec utworu
                    System.out.println("End song");
                    if (status.loopType == LoopType.RepeatAll) {
                        controller.nextSong();
                    } else if (status.loopType == LoopType.RepeatOne) {
                        reload();
                    } else { // random
                        //TODO Usprawnic losowa kolejnosc
                        Random generator = new Random();
                        int rand = generator.nextInt() % status.currentPlaylist.getSongsAmount();
                        for (int i = 0; i < rand; ++i)
                            controller.nextSong();
                    }
                }
            }
        });

        mediaPlayer.setVolume(status.volumeValue);
        //path = String.valueOf(duration);
        return true;
    }

    boolean pause() {
        if (status.played && !status.paused) {
            mediaPlayer.pause();
            status.paused = true;
        } else if (status.played && status.paused) {
            mediaPlayer.play();
            status.paused = false;
        }
        return status.paused;
    }

    void reload() {
        mediaPlayer.seek(mediaPlayer.getStartTime());
    }

    String getPath() {
        return status.path;
    }

    public void setPath(String path) {
        this.status.path = path;
    }

    public Duration getAllDuration() {
        return status.allDuration;
    }

    public Duration getCurrentDuration() {
        return mediaPlayer.getCurrentTime();
    }

    void changeTime(Double val) {
        mediaPlayer.seek(status.allDuration.multiply(val));
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setVolume(double vol) {
        mediaPlayer.setVolume(vol);
        status.volumeValue = vol;
    }

    public String parseTime(Duration d) {
        int minutes = (int) d.toMinutes();
        int seconds = (int) d.toSeconds();
        seconds = seconds - minutes * 60;

        String time = minutes + ":";
        if (seconds < 10) time += "0";
        time += Integer.toString(seconds);
        return time;
    }

    public Playlist getCurrentPlaylist() {
        return status.currentPlaylist;
    }

    public boolean addSongToCurrentPlaylist(Song song) {
        boolean result = status.currentPlaylist.addSong(song);
        if (result) {
            playlists.writePlaylistsToFile("");
            Media localMedia = new Media(new File(song.getSongPath()).toURI().toString());
            localMediaPlayer = new MediaPlayer(localMedia);

            localMediaPlayer.setOnReady(new Runnable() {
                public void run() {
                    Duration localDuration = localMediaPlayer.getMedia().getDuration();
                    System.out.println("Czas dodanego: " + parseTime(localDuration));
                    //song.setSongTime(parseTime(localDuration));
                    status.currentPlaylist.getSongByIndex(status.currentPlaylist.getSongsAmount() - 1).setSongTime(parseTime(localDuration));
                }
            });
        }
        return result;
    }

    public void nextSong() {
        int nextIndex = (status.currentlyPlayedSongIndex + 1) % status.currentPlaylist.getSongsAmount();
        String path = status.currentPlaylist.getPathOfSong(nextIndex);
        if (path != null) {
            setPath(path);
            setTitle(status.currentPlaylist.getSongByIndex(nextIndex).getSongName());
            status.currentlyPlayedSongIndex = nextIndex;
        }
    }

    public void prevSong() {
        int nextIndex = (status.currentlyPlayedSongIndex - 1) % status.currentPlaylist.getSongsAmount();
        if (status.currentlyPlayedSongIndex == 0) nextIndex = status.currentPlaylist.getSongsAmount() - 1;
        String path = status.currentPlaylist.getPathOfSong(nextIndex);
        if (path != null) {
            setPath(path);
            setTitle(status.currentPlaylist.getSongByIndex(nextIndex).getSongName());
            status.currentlyPlayedSongIndex = nextIndex;
        }
    }

    public void setSong(String ti, String pt) {
        setPath(pt);
        setTitle(ti);
        status.currentlyPlayedSongIndex = status.currentPlaylist.getIndexOfSong(ti);
        status.played = false;
    }

    public void setRepeatMode(MP3Player.LoopType type) {
        status.loopType = type;
    }

    public void setTitle(String title) {
        status.title = title;
    }

    public double getRate() {
        return status.rate;
    }

    public void setRate(double r) {
        if (r <= 0 || r >= 5)
            return;
        status.rate = r;
        mediaPlayer.setRate(r);
    }

    public boolean deleteSong(String title) {
        String saveCurrentTitle = status.currentPlaylist.getSongByIndex(status.currentlyPlayedSongIndex).getSongName();
        if (saveCurrentTitle.equals(title)) {
            return false;
        }
        status.currentPlaylist.deleteSongByTitle(title);
        ArrayList<Song> s = status.currentPlaylist.getAllSongs();
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).getSongName().equals(saveCurrentTitle)) {
                status.currentlyPlayedSongIndex = i;
                playlists.writePlaylistsToFile("");
                return true;
            }
        }
        playlists.writePlaylistsToFile("");
        //TODO napisac co sie stanie jak sie usunelo jedyna piosenke
        return false;
    }

    public void resetAppData() {
        playlists = null;
        playlists = new AllPlaylists();
        status = new PlayerStatus();
        playlists.writePlaylistsToFile("");
    }

    public enum LoopType {
        RepeatAll, RepeatOne, Random
    }

    public class PlayerStatus {
        public boolean played = false;
        public boolean paused = false;
        public Playlist currentPlaylist;
        public Duration allDuration;
        public Duration currentDuration;
        public double volumeValue = 1;
        public int currentlyPlayedSongIndex = 0;
        public String path;
        public String title;
        public LoopType loopType = LoopType.RepeatAll;
        public double rate = 1;
    }


}
