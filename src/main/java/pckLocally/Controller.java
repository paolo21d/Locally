package pckLocally;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    private  MP3Player player= new MP3Player();
    Communication communication = new Communication(player);
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
}
