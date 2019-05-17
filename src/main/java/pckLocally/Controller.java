package pckLocally;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    AllPlaylists playlists = new AllPlaylists();
    boolean connection = false;
    boolean played = false;
    ObservableList<Song> observableList = FXCollections.observableArrayList();
    Playlist playlist = new Playlist();
    MP3Player.LoopType loopType = MP3Player.LoopType.RepeatAll;
    boolean inited = false;
    //private MP3Player player = new MP3Player(this);
    //Communication communication;
    private double volumeValue;
    private boolean mute = false;
    @FXML
    private VBox mainBox;
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
    private TableView<SongTable> TablePlaylist;
    @FXML
    private TableColumn<SongTable, String> SongColumn;
    @FXML
    private TableColumn<SongTable, String> TimeColumn;
    @FXML
    private ImageView playPauseImage;
    @FXML
    private ImageView loopImage;
    @FXML
    private ImageView volumeIcon;
    @FXML
    private Button speedButton;

    public Controller() {
        MP3Player.getInstance().setController(this);
        Communication.getInstance().init(this, MP3Player.getInstance().getStatus());
//        if (MP3Player.getInstance().getStatus().played) {//
//            playPauseImage.setImage(new Image("/icons/pause.png"));
//            played = true;
//        }
    }

    @FXML
    void chooseFileButtonClick(ActionEvent event) {
        /*FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MUSIC files (.mp3)", "*.mp3");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(null);
        String path = file.getAbsolutePath();
        path = path.replace("\\", "/");
        player.setPath(path);*/
        addSongByClick();

    }
    public void addSongByClick(){
        String path;
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MUSIC files (.mp3)", "*.mp3");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(null);
        if (file == null)
            return;
        path = file.getAbsolutePath().replace("\\", "/");
        String[] fullPath = path.split("/");
        String title = fullPath[fullPath.length - 1];

        Song song = new Song(title, "-", path);
        if (MP3Player.getInstance().addSongToCurrentPlaylist(song))
            TablePlaylist.getItems().add(new SongTable(song));

        Communication.getInstance().sendStatus();
    }

    @FXML
    void choosePlaylistButtonClick(ActionEvent event) {
        System.out.println("CHOOSE PLAYLIST");

    }

    @FXML
    void connectButtonClick(ActionEvent event) {
        if (!connection) {
            Communication.getInstance().start();
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

    public synchronized void nextSong() {
        MP3Player.getInstance().nextSong();
        played = false;
        MP3Player.getInstance().getStatus().played = false;
        playPause();
        //communication.sendStatus();
    }

    @FXML
    void prevButtonClick(ActionEvent event) {
        System.out.println("PREV");
        prevSong();
    }

    public synchronized void prevSong() {
        MP3Player.getInstance().prevSong();
        MP3Player.getInstance().getStatus().played = false;
        played = false;
        playPause();
        //communication.sendStatus();
    }

    @FXML
    void repeatButtonClick(ActionEvent event) {
        repeat();
    }

    public synchronized void repeat() {
        if (played) {
            MP3Player.getInstance().reload();
            //playPauseButton.setText("Pause");
            playPauseImage.setImage(new Image("/icons/pause.png"));
            Communication.getInstance().sendStatus();
        }
    }

    public void loopButtonClick(ActionEvent actionEvent) {
        loopChange();
    }

    public synchronized void loopChange() {
        if (loopType == MP3Player.LoopType.RepeatAll) {
            //loopButton.setText("1Repeat");
            loopImage.setImage(new Image("/icons/repeatOne.png"));
            MP3Player.getInstance().setRepeatMode(MP3Player.LoopType.RepeatOne);
            loopType = MP3Player.LoopType.RepeatOne;
        } else if (loopType == MP3Player.LoopType.RepeatOne) {
            //loopButton.setText("Random");
            loopImage.setImage(new Image("/icons/random.png"));
            MP3Player.getInstance().setRepeatMode(MP3Player.LoopType.Random);
            loopType = MP3Player.LoopType.Random;
        } else { //loopType == Random
            //loopButton.setText("A Repeat");
            loopImage.setImage(new Image("/icons/repeatAll.png"));
            MP3Player.getInstance().setRepeatMode(MP3Player.LoopType.RepeatAll);
            loopType = MP3Player.LoopType.RepeatAll;
        }
        Communication.getInstance().sendStatus();
    }

    /////////////////INITIALIZE
    public void initialize(URL location, ResourceBundle resources) {
        if (MP3Player.getInstance().getStatus().played) {//
            playPauseImage.setImage(new Image("/icons/pause.png"));
            labelSongDescription.setText(MP3Player.getInstance().getStatus().currentPlaylist.getSongByIndex(MP3Player.getInstance().getStatus().currentlyPlayedSongIndex).getSongName());
            volumeSlider.setValue(MP3Player.getInstance().getStatus().volumeValue*100);
            changeVolume();
            volumeValue = volumeSlider.getValue(); //TODO naprawic slider po zmianie widoku
            played = true;
        }
        if(MP3Player.getInstance().getStatus().paused){
            playPauseImage.setImage(new Image("/icons/play.png"));
        }

        inited = true;
        SongColumn.setCellValueFactory(new PropertyValueFactory<SongTable, String>("SongName"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<SongTable, String>("SongTime"));

        //TablePlaylist.setItems(observableList);
        //Song initSong = new Song("ts22xxx.mp3", "-", player.getPath());
        //TablePlaylist.getItems().add(initSong);
        //playlist.addSong(initSong);

        playlist = MP3Player.getInstance().getCurrentPlaylist();
        if (playlist != null) {
            for (int i = 0; i < playlist.getSongsAmount(); ++i) {
                TablePlaylist.getItems().add(new SongTable(playlist.getSongByIndex(i)));
            }
        }

        volumeSlider.setValue(100);

        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isPressed() && played) {
                    changeTime();
                }
            }
        });
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                if (volumeSlider.isPressed() && played) {
                    changeVolume();
                } else if (!played) {
                    volumeValue = 100;
                    volumeSlider.setValue(100);
                }
            }
        });

    }

    public void updateValuesTime() {
        timeSlider.setValue(MP3Player.getInstance().getCurrentDuration().toMillis() / MP3Player.getInstance().getAllDuration().toMillis() * 100);
        labelTime.setText(MP3Player.getInstance().parseTime(MP3Player.getInstance().getCurrentDuration()) + " / " + MP3Player.getInstance().parseTime(MP3Player.getInstance().getAllDuration()));
    }

    public void changeTime() {
        MP3Player.getInstance().changeTime(timeSlider.getValue() / 100);
        //System.out.println(timeSlider.getValue());
        Communication.getInstance().sendStatus();
    }

    public void changeVolume() {
        volumeValue = volumeSlider.getValue();
        //volumeSlider.setValue(volumeValue);
        MP3Player.getInstance().setVolume(volumeValue / 100);
        labelVolume.setText(Integer.toString((int) volumeValue) + "%");
        if (volumeValue == 0) {
            volumeIcon.setImage(new Image("/icons/volumeMute.png"));
            mute = true;
        } else if (volumeValue < 50) {
            volumeIcon.setImage(new Image("/icons/volumeLow.png"));
            mute = false;
        } else {
            volumeIcon.setImage(new Image("/icons/volumeHigh.png"));
            mute = false;
        }
        Communication.getInstance().sendStatus();
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
        addSongByClick();
    }

    public void tableClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String title = TablePlaylist.getSelectionModel().getSelectedItem().getSongName();
            String time = TablePlaylist.getSelectionModel().getSelectedItem().getSongTime();
            String path = TablePlaylist.getSelectionModel().getSelectedItem().getSongPath();
            System.out.println(title);

            setSong(title, path);
        }
    }

    public void setSong(String title, String path) {
        MP3Player.getInstance().setSong(title, path);
        played = false;
        playPause();
    }

    public synchronized void playPause() {
        if (!MP3Player.getInstance().getStatus().played) { //start play
            labelSongDescription.setText("Opening...");
            try {
                MP3Player.getInstance().play();
            } catch (Exception e) {
                labelSongDescription.setText("File Open Error. Choose right path to file...");
                return;
            }
            String[] fullPath = MP3Player.getInstance().getPath().split("/");
            String title = fullPath[fullPath.length - 1];
            labelSongDescription.setText(title);
            MP3Player.getInstance().setTitle(title);
            //Thread.sleep(2000);
            //TablePlaylist.getItems().add(new Song(title, player.parseTime(player.getAllDuration()), player.getPath()));
            //TablePlaylist.getItems().add(new Song(title, "unknown", player.getPath()));

            //playPauseButton.setText("Pause");
            playPauseImage.setImage(new Image("/icons/pause.png"));
            played = true;
        } else { //pause, continue
            boolean tmp = MP3Player.getInstance().pause();
            if (tmp)
                //playPauseButton.setText("Play");
                playPauseImage.setImage(new Image("/icons/play.png"));
            else
                //playPauseButton.setText("Pause");
                playPauseImage.setImage(new Image("/icons/pause.png"));
        }
        Communication.getInstance().sendStatus();
    }

    public void setSongTimePalaylist(String time, String name) {
        TablePlaylist.getItems().iterator();

    }

    public void volumeImageClicked(MouseEvent mouseEvent) {
        //TODO naprawic po wyciszeniu zeby wracalo do dobrego stanu
        if (played && !mute) {
            volumeValue = volumeSlider.getValue();
            mute = true;
            MP3Player.getInstance().setVolume(0);
            labelVolume.setText("0%");
            volumeSlider.setValue(0);
            volumeIcon.setImage(new Image("/icons/volumeMute.png"));
        } else if (played && mute) {
            MP3Player.getInstance().setVolume(volumeValue / 100);
            labelVolume.setText(Integer.toString((int) volumeValue) + "%");
            volumeSlider.setValue(volumeValue / 100);
            mute = false;
            if (volumeValue < 50) {
                volumeIcon.setImage(new Image("/icons/volumeLow.png"));
            } else {
                volumeIcon.setImage(new Image("/icons/volumeHigh.png"));
            }
        }
        Communication.getInstance().sendStatus();
    }

    public void speedButtonClick(ActionEvent actionEvent) {
        if (MP3Player.getInstance().getRate() == 1) {
            MP3Player.getInstance().setRate(1.5);
            speedButton.setText("x1.5");
        } else if (MP3Player.getInstance().getRate() == 1.5) {
            MP3Player.getInstance().setRate(2);
            speedButton.setText("x2");
        } else if (MP3Player.getInstance().getRate() == 2) {
            MP3Player.getInstance().setRate(0.5);
            speedButton.setText("x0.5");
        } else if (MP3Player.getInstance().getRate() == 0.5) {
            MP3Player.getInstance().setRate(0.75);
            speedButton.setText("x0.75");
        } else if (MP3Player.getInstance().getRate() == 0.75) {
            MP3Player.getInstance().setRate(1);
            speedButton.setText("x1");
        }
    }

    public void minimalizeButtonClick(ActionEvent actionEvent) {
        try {
            VBox main2 = FXMLLoader.load(getClass().getResource("/layoutMinimalize.fxml"));
            //MP3Player.getInstance().pause();
            Main.mainStage.setWidth(255);
            Main.mainStage.setHeight(130);
            mainBox.getChildren().setAll(main2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void menuDeleteSongFromPlaylistClick(ActionEvent actionEvent) {
        List<String> choices = new ArrayList<String>();
        for(Song s: MP3Player.getInstance().getStatus().currentPlaylist.getAllSongs()){
            choices.add(s.getSongName());
        }
        if(choices.size() == 0)
            return;
        ChoiceDialog<String> dialog = new ChoiceDialog<String>(MP3Player.getInstance().getStatus().currentPlaylist.getSongByIndex(0).getSongName(), choices);
        dialog.setTitle("Delete song");
        dialog.setHeaderText("Choose song, which you want to delete");
        dialog.setContentText("Song name: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            System.out.println("To delete: " + result.get());
            boolean r = MP3Player.getInstance().deleteSong(result.get());
            if(r){
                TablePlaylist.getItems().clear();
                for(Song s: MP3Player.getInstance().getStatus().currentPlaylist.getAllSongs()){
                    TablePlaylist.getItems().add(new SongTable(s));
                }
            }
        }
    }

    public void menuChoosePlaylist(ActionEvent actionEvent) {
        List<String> choices = new ArrayList<String>();
        for(Playlist p: MP3Player.getInstance().getPlaylists().getAllPlaylists()){
            choices.add(p.getPlName());
        }
        if(choices.size() == 0)
            return;
        ChoiceDialog<String> dialog = new ChoiceDialog<String>(MP3Player.getInstance().getPlaylists().getAllPlaylists().get(0).getPlName(), choices);
        dialog.setTitle("Choose Playlist");
        dialog.setHeaderText("Choose playlist, which you want to play");
        dialog.setContentText("Playlist name: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            System.out.println("Chosen playlist: " + result.get());
            boolean r = MP3Player.getInstance().pickPlaylist(result.get());
            if(r){
                TablePlaylist.getItems().clear();
                for(Song s: MP3Player.getInstance().getStatus().currentPlaylist.getAllSongs()){
                    TablePlaylist.getItems().add(new SongTable(s));
                }
            }
        }
    }

    public void menuAddPlaylist(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("NewPlaylist");
        dialog.setTitle("Add playlist");
        dialog.setHeaderText("Input playlist name");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            System.out.println("Your name: " + result.get());
            AllPlaylists ap = MP3Player.getInstance().getPlaylists();
            Playlist p = new Playlist(result.get());
            ap.addPlaylist(p);
        }
    }

    public void helpConnect(ActionEvent actionEvent) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("How to connect?");
        alert.setHeaderText("Connection");
        alert.setContentText("Firstly press in this application button connect (right bottom corner), next press connect button in client application.");

        alert.showAndWait();
    }
}

