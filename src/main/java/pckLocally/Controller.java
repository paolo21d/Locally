package pckLocally;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private  MP3Player player= new MP3Player();
    Communication communication = new Communication(player);
    AllPlaylists playlists = new AllPlaylists();
    boolean connection=false;


    @FXML
    private Button playButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button reloadButton;
    @FXML
    private Button chooseFileButton;
    @FXML
    private Button choosePlaylistButton;
    @FXML
    private Slider timeSlider;
    @FXML
    private Label labelSongDescription;
    @FXML
    private Button connectButton;

    @FXML
    private TableView<Song> TablePlaylist;
    @FXML
    private TableColumn<Song, String> SongColumn;
    @FXML
    private TableColumn<Song, String> TimeColumn;


    @FXML
    void chooseFileButtonClick(ActionEvent event) {
        player.setPath();
    }
    @FXML
    void choosePlaylistButtonClick(ActionEvent event) {
        System.out.println("CHOOSE PLAYLIST");
    }

    @FXML
    void connectButtonClick(ActionEvent event) {
        if(!connection){
            communication.start();
            connection=true;
        }
    }

    @FXML
    void pauseButtonClick(ActionEvent event) {
        boolean tmp = player.pause();
        if(tmp)
            pauseButton.setText("Play");
        else
            pauseButton.setText("Pause");
    }

    @FXML
    void playButtonClick(ActionEvent event) {
        labelSongDescription.setText("Opening...");
        try{
            player.play();
        }catch(Exception e){
            labelSongDescription.setText("File Open Error. Choose right path to file...");
            return;
        }
        String [] fullPath = player.getPath().split("/");
        String title = fullPath[fullPath.length-1];
        labelSongDescription.setText(title);
        pauseButton.setText("Pause");

        ///fill table
        Song song = new Song(title,"-");
        //TablePlaylist.getItems().add(song);
    }

    @FXML
    void reloadButtonClick(ActionEvent event) {
        player.reload();
        pauseButton.setText("Pause");
    }

    public void initialize(URL location, ResourceBundle resources) {
        SongColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("SongName"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("SongTime"));

        TablePlaylist.setItems(observableList);

        Playlist pl1 = new Playlist();
        pl1.addSong(new Song("s1", "1"));
        pl1.addSong(new Song("s2", "2"));

        Playlist pl2 = new Playlist();
        pl2.addSong(new Song("s3", "3"));
        pl2.addSong(new Song("s4", "4"));

        playlists.addPlaylist(pl1);
        playlists.addPlaylist(pl2);

        Gson json = new Gson();
        String response = json.toJson(playlists);
        //System.out.println(response);
    }
    ObservableList<Song> observableList = FXCollections.observableArrayList(
      new Song("Nazwa", "czas"),
      new Song("Nazwa2", "czas2")
    );
}
