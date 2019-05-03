package pckLocally;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;

public class MP3Player {
    Controller controller;
    private String path;
    private Duration duration = null;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private boolean played = false;
    private boolean paused = false;
    private AllPlaylists playlists;

    public MP3Player(Controller c) {
        path = "C:/Users/paolo/Desktop/Java Start/MP3 V2/src/sample/TS22.mp3";
        controller = c;
    }

    public MP3Player() {
        path = "C:/Users/paolo/Desktop/Java Start/MP3 V2/src/sample/TS22.mp3";
    }

    void play() {
        if (path == null) {
            System.out.println("Path is null");
            return;
        }

        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            System.out.println(status);
            mediaPlayer.pause();
        }
        played = true;
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        //mediaPlayer.setAutoPlay(true);
        //mediaPlayer.play();
        mediaView = new MediaView(mediaPlayer);
        mediaPlayer.play();


        mediaPlayer.setOnReady(new Runnable() {
            public void run() {
                duration = mediaPlayer.getMedia().getDuration();
                System.out.println("Czas trwania " + parseTime(duration));

            }
        });

        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                controller.updateValuesTime();
            }
        });

        mediaPlayer.setVolume(1);
        //path = String.valueOf(duration);
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

    void setPath(String path) {
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
    }

    public String parseTime(Duration d) {
        int minutes = (int) d.toMinutes();
        int seconds = (int) d.toSeconds();
        seconds = seconds - minutes * 60;

        String time = Integer.toString(minutes) + ":";
        if (seconds < 10) time += "0";
        time += Integer.toString(seconds);
        return time;
    }
    /*public boolean writePlaylistsToFile(){
        String path = new String("playlists.json");
        Gson json = new Gson();
        String response = json.toJson(playlists);

        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(response);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return  false;
        }
        return true;
    }
    public boolean readPlaylistsFromFile(){
        String path = new String("playlists.json");
        String data = new String("");
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return  false;
        }

        Gson json = new Gson();
        playlists = json.fromJson(data, AllPlaylists.class);
        return true;
    }*/
}
