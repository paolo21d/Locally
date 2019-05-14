package pckLocally;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;

public class MP3Player {
    Controller controller;
    //boolean loopPlaylist = true;
    public LoopType loopType = LoopType.RepeatAll;
    private String path;
    private Duration duration = null;
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean played = false;
    private boolean paused = false;
    private AllPlaylists playlists = new AllPlaylists();
    private Playlist currentPlaylist;
    private int currentlyPlayedSongIndex = 0;
    private double volumeValue = 1;
    private MediaPlayer localMediaPlayer;


    public MP3Player(Controller c) {
        path = "C:/Users/paolo/Desktop/Java Start/MP3 V2/src/sample/TS22.mp3";
        controller = c;
        playlists.readPlaylistsFromFile();
        currentPlaylist = playlists.getPlaylistByName("default");
    }

    public MP3Player() {
        path = "C:/Users/paolo/Desktop/Java Start/MP3 V2/src/sample/TS22.mp3";
    }

    public enum LoopType{
        RepeatAll, RepeatOne, Random;
    }
    boolean play() {
        if (path == null) {
            System.out.println("Path is null");
            return false;
        }
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            System.out.println(status);
            mediaPlayer.pause();
        }
        played = true;
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();


        mediaPlayer.setOnReady(new Runnable() {
            public void run() {
                duration = mediaPlayer.getMedia().getDuration();
                System.out.println("Czas trwania " + parseTime(duration));
                currentPlaylist.getSongByIndex(currentlyPlayedSongIndex).setSongTime(parseTime(duration));
            }
        });

        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                controller.updateValuesTime();
                if (getAllDuration().toMillis() - getCurrentDuration().toMillis() <= 500) { //koniec utworu
                    System.out.println("End song");
                    if(loopType == LoopType.RepeatAll){
                        controller.nextSong();
                    }else if(loopType==LoopType.RepeatOne){
                        reload();
                    }else{ // random
                        //TODO Usprawnic losowa kolejnosc
                        Random generator = new Random();
                        int rand = generator.nextInt() % currentPlaylist.getSongsAmount();
                        for(int i=0; i<rand; ++i)
                            controller.nextSong();
                    }
                }
            }
        });

        mediaPlayer.setVolume(volumeValue);
        //path = String.valueOf(duration);
        return true;
    }

    boolean pause() {
        if (played && !paused) {
            mediaPlayer.pause();
            paused = true;
        } else if (played && paused) {
            mediaPlayer.play();
            paused = false;
        }
        return paused;
    }

    void reload() {
        mediaPlayer.seek(mediaPlayer.getStartTime());
    }

    String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Duration getAllDuration() {
        return duration;
    }

    public Duration getCurrentDuration() {
        return mediaPlayer.getCurrentTime();
    }

    void changeTime(Double val) {
        mediaPlayer.seek(duration.multiply(val));
    }

    public Status getStatus() {
        return mediaPlayer.getStatus();
    }

    public void setVolume(double vol) {
        mediaPlayer.setVolume(vol);
        volumeValue = vol;
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
        return currentPlaylist;
    }

    public boolean addSongToCurrentPlaylist(Song song) {
        boolean result = currentPlaylist.addSong(song);
        if (result) {
            playlists.writePlaylistsToFile();
            Media localMedia = new Media(new File(song.getSongPath()).toURI().toString());
            localMediaPlayer = new MediaPlayer(localMedia);

            localMediaPlayer.setOnReady(new Runnable() {
                public void run() {
                    Duration localDuration = localMediaPlayer.getMedia().getDuration();
                    System.out.println("Czas dodanego: " + parseTime(localDuration));
                    //song.setSongTime(parseTime(localDuration));
                    currentPlaylist.getSongByIndex(currentPlaylist.getSongsAmount() - 1).setSongTime(parseTime(localDuration));
                }
            });
        }
        return result;
    }

    public void nextSong() {
        int nextIndex = (currentlyPlayedSongIndex + 1) % currentPlaylist.getSongsAmount();
        String path = currentPlaylist.getPathOfSong(nextIndex);
        if (path != null) {
            setPath(path);
            currentlyPlayedSongIndex = nextIndex;
        }
    }

    public void prevSong() {
        int nextIndex = (currentlyPlayedSongIndex - 1) % currentPlaylist.getSongsAmount();
        if (currentlyPlayedSongIndex == 0) nextIndex = currentPlaylist.getSongsAmount() - 1;
        String path = currentPlaylist.getPathOfSong(nextIndex);
        if (path != null) {
            setPath(path);
            currentlyPlayedSongIndex = nextIndex;
        }
    }

    public void setSong(String ti, String pt) {
        setPath(pt);
        currentlyPlayedSongIndex = currentPlaylist.getIndexOfSong(ti);
    }
}
