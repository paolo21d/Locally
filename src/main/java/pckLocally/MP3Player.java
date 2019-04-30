package pckLocally;

import com.google.gson.Gson;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MP3Player {
    private String path;
    Duration duration=null;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private boolean played=false;
    private boolean paused=false;
    private AllPlaylists playlists;

    public MP3Player() {
        path = "C:/Users/paolo/Desktop/Java Start/MP3 V2/src/sample/TS22.mp3";
    }

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
        mediaPlayer.setOnReady(new Runnable() {
            public void run() {
                System.out.println("Czas trwania" + media.getDuration().toSeconds());
            }
        });
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
