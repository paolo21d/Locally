package pckLocally;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class MP3Player {
    private String path="C:\\Users\\paolo\\Desktop\\Java Start\\MP3 V2\\src\\sample\\TS22.mp3";
    Duration duration=null;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private boolean played=false;
    private boolean paused=false;

    void play(){
        if(path==null) {
            System.out.println("Path is null");
            return;
        }

        if(mediaPlayer!=null){
            MediaPlayer.Status status = mediaPlayer.getStatus();
            System.out.println(status);
            mediaPlayer.pause();
        }
        played=true;
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        //mediaPlayer.setAutoPlay(true);
        //mediaPlayer.play();
        mediaView = new MediaView(mediaPlayer);
        mediaPlayer.play();

        duration = mediaPlayer.getMedia().getDuration();
        //path = String.valueOf(duration);
    }
    boolean pause(){
        if(played && !paused) {
            mediaPlayer.pause();
            paused=true;
        } else if(played && paused) {
            mediaPlayer.play();
            paused=false;
        }
        return paused;
    }
    void reload(){
        mediaPlayer.seek(mediaPlayer.getStartTime());
    }
    void setPath(){
        FileChooser fc = new FileChooser();
        //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.mp3"));
        File file = fc.showOpenDialog(null);
        path = file.getAbsolutePath();
        path = path.replace("\\", "/");
    }
    String getPath(){
        return path;
    }
}
