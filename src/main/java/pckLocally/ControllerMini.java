package pckLocally;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMini implements Initializable {

    public VBox mainMini;
    public Label labelInfo;
    public ImageView minPlayPauseImage;

    public void miniPrevClick(ActionEvent actionEvent) {
        MP3Player.getInstance().prevSong();
        MP3Player.getInstance().getStatus().played = false;
        MP3Player.getInstance().play();
        String[] fullPath = MP3Player.getInstance().getPath().split("/");
        String title = fullPath[fullPath.length - 1];
        labelInfo.setText(title);
        MP3Player.getInstance().setTitle(title);
    }

    public void minNextClick(ActionEvent actionEvent) {
        MP3Player.getInstance().nextSong();
        MP3Player.getInstance().getStatus().played = false;
        MP3Player.getInstance().play();
        String[] fullPath = MP3Player.getInstance().getPath().split("/");
        String title = fullPath[fullPath.length - 1];
        labelInfo.setText(title);
        MP3Player.getInstance().setTitle(title);
    }

    public void minPlayPauseClick(ActionEvent actionEvent) {
        if (!MP3Player.getInstance().getStatus().played) { //start play
            try {
                MP3Player.getInstance().play();
            } catch (Exception e) {
                labelInfo.setText("File Open Error. Choose right path to file...");
                return;
            }
            String[] fullPath = MP3Player.getInstance().getPath().split("/");
            String title = fullPath[fullPath.length - 1];
            labelInfo.setText(title);
            MP3Player.getInstance().setTitle(title);
            //Thread.sleep(2000);
            //TablePlaylist.getItems().add(new Song(title, player.parseTime(player.getAllDuration()), player.getPath()));
            //TablePlaylist.getItems().add(new Song(title, "unknown", player.getPath()));

            //playPauseButton.setText("Pause");
            minPlayPauseImage.setImage(new Image("/icons/pause.png"));
        } else { //pause, continue
            boolean tmp = MP3Player.getInstance().pause();
            if (tmp)
                minPlayPauseImage.setImage(new Image("/icons/play.png"));
            else
                minPlayPauseImage.setImage(new Image("/icons/pause.png"));
        }
        Communication.getInstance().sendStatus();
    }

    public void minChangeViewClick(ActionEvent actionEvent) {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/layoutMain.fxml"));
            Main.mainStage.setWidth(605);
            Main.mainStage.setHeight(375);
            mainMini.getChildren().setAll(box);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        if(MP3Player.getInstance().getStatus().played){
            labelInfo.setText(MP3Player.getInstance().getStatus().title);
        }else{
            minPlayPauseImage.setImage(new Image("/icons/play.png"));
        }
    }
}
