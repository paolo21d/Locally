package pckLocally;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

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
    private TableView<?> TablePlaylist;
    @FXML
    private TableColumn<?, ?> SongColumn;
    @FXML
    private TableColumn<?, ?> TimeColumn;


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
        player.play();
        labelSongDescription.setText(player.getPath());
        pauseButton.setText("Pause");
    }

    @FXML
    void reloadButtonClick(ActionEvent event) {
        player.reload();
        pauseButton.setText("Pause");
    }
}
