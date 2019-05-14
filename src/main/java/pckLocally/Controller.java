package pckLocally;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    AllPlaylists playlists = new AllPlaylists();
    boolean connection = false;
    boolean played = false;
    ObservableList<Song> observableList = FXCollections.observableArrayList();
    Playlist playlist = new Playlist();
    Communication communication = new Communication(this);
    private MP3Player player = new MP3Player(this);
    MP3Player.LoopType loopType = MP3Player.LoopType.RepeatAll;
    @FXML
    private Button playPauseButton;
    @FXML
    private Button reloadButton;
    @FXML
    private Button chooseFileButton;
    @FXML
    private Button choosePlaylistButton;
    @FXML
    private Label labelSongDescription;
    @FXML
    private Button connectButton;
    @FXML
    private Button loopButton;
    @FXML
    private Label labelTime;
    @FXML
    private Label labelVolume;
    @FXML
    private Slider timeSlider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private TableView<Song> TablePlaylist;
    @FXML
    private TableColumn<Song, String> SongColumn;
    @FXML
    private TableColumn<Song, String> TimeColumn;

    @FXML
    void chooseFileButtonClick(ActionEvent event) {
        /*FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MUSIC files (.mp3)", "*.mp3");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(null);
        String path = file.getAbsolutePath();
        path = path.replace("\\", "/");
        player.setPath(path);*/

        String path;
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MUSIC files (.mp3)", "*.mp3");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(null);
        if(file == null)
            return;
        path = file.getAbsolutePath().replace("\\", "/");
        String[] fullPath = path.split("/");
        String title = fullPath[fullPath.length - 1];

        Song song = new Song(title, "-", path);
        if (player.addSongToCurrentPlaylist(song))
            TablePlaylist.getItems().add(song);
    }

    @FXML
    void choosePlaylistButtonClick(ActionEvent event) {
        System.out.println("CHOOSE PLAYLIST");

    }

    @FXML
    void connectButtonClick(ActionEvent event) {
        if (!connection) {
            communication.start();
            connection = true;
        }
    }

    @FXML
    void playPauseButtonClick(ActionEvent event) throws InterruptedException {
        playPause();
    }

    @FXML
    void nextButtonClick(ActionEvent event) {
        System.out.println("NEXT");
        nextSong();
    }

    public void nextSong() {
        player.nextSong();
        played = false;
        playPause();
    }

    @FXML
    void prevButtonClick(ActionEvent event) {
        System.out.println("PREV");
        prevSong();
    }

    public void prevSong() {
        player.prevSong();
        played = false;
        playPause();
    }

    @FXML
    void repeatButtonClick(ActionEvent event) {
        if (played) {
            player.reload();
            playPauseButton.setText("Pause");
        }
    }

    public void loopButtonClick(ActionEvent actionEvent) {
        if (loopType == MP3Player.LoopType.RepeatAll){
            loopButton.setText("1Repeat");
            player.loopType = MP3Player.LoopType.RepeatOne;
            loopType = MP3Player.LoopType.RepeatOne;
        }else if(loopType == MP3Player.LoopType.RepeatOne){
            loopButton.setText("Random");
            player.loopType = MP3Player.LoopType.Random;
            loopType = MP3Player.LoopType.Random;
        }else{ //loopType == Random
            loopButton.setText("A Repeat");
            player.loopType = MP3Player.LoopType.RepeatAll;
            loopType = MP3Player.LoopType.RepeatAll;
        }
    }

    /////////////////INITIALIZE
    public void initialize(URL location, ResourceBundle resources) {
        SongColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("SongName"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("SongTime"));

        //TablePlaylist.setItems(observableList);
        //Song initSong = new Song("ts22xxx.mp3", "-", player.getPath());
        //TablePlaylist.getItems().add(initSong);
        //playlist.addSong(initSong);

        playlist = player.getCurrentPlaylist();
        for (int i = 0; i < playlist.getSongsAmount(); ++i) {
            TablePlaylist.getItems().add(playlist.getSongByIndex(i));
        }

        volumeSlider.setValue(100);

        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isPressed() && played) {
                    player.changeTime(timeSlider.getValue() / 100);
                    //System.out.println(timeSlider.getValue());
                }
            }
        });
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                if (volumeSlider.isPressed() && played) {
                    double value = volumeSlider.getValue();
                    player.setVolume(value / 100);
                    labelVolume.setText(Integer.toString((int) value) + "%");
                } else if (!played) {
                    volumeSlider.setValue(100);
                }
            }
        });
    }

    public void updateValuesTime() {
        timeSlider.setValue(player.getCurrentDuration().toMillis() / player.getAllDuration().toMillis() * 100);
        labelTime.setText(player.parseTime(player.getCurrentDuration()) + " / " + player.parseTime(player.getAllDuration()));
    }

    public void helpAbout(ActionEvent actionEvent) {
        System.out.println("ABOUT");

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Locally - mp3 player which can be controlled by various devices.");
        alert.setContentText("Author: Pawel Swiatkowski\nVersion: 1.0");

        alert.showAndWait();
    }

    public void menuPlaylistAddSongClicked(ActionEvent actionEvent) {
    }

    public void tableClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String title = TablePlaylist.getSelectionModel().getSelectedItem().getSongName();
            String time = TablePlaylist.getSelectionModel().getSelectedItem().getSongTime();
            String path = TablePlaylist.getSelectionModel().getSelectedItem().getSongPath();
            System.out.println(title);

            player.setSong(title, path);
            played = false;
            playPause();
        }
    }

    public void playPause() {
        if (!played) { //start play
            labelSongDescription.setText("Opening...");
            try {
                player.play();
            } catch (Exception e) {
                labelSongDescription.setText("File Open Error. Choose right path to file...");
                return;
            }
            String[] fullPath = player.getPath().split("/");
            String title = fullPath[fullPath.length - 1];
            labelSongDescription.setText(title);
            //Thread.sleep(2000);
            //TablePlaylist.getItems().add(new Song(title, player.parseTime(player.getAllDuration()), player.getPath()));
            //TablePlaylist.getItems().add(new Song(title, "unknown", player.getPath()));

            playPauseButton.setText("Pause");
            played = true;
        } else { //pause, continue
            boolean tmp = player.pause();
            if (tmp)
                playPauseButton.setText("Play");
            else
                playPauseButton.setText("Pause");
        }
    }

    public void setSongTimePalaylist(String time, String name){
        TablePlaylist.getItems().iterator();

    }

    public void volumeImageClicked(MouseEvent mouseEvent) {
        //TODO napisac mute, volume, zmiane obrazka wraz ze zmiana volume slidera
    }
}

