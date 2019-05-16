package pckLocally;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
        MP3Player.getInstance().play();
        String[] fullPath = MP3Player.getInstance().getPath().split("/");
        String title = fullPath[fullPath.length - 1];
        labelInfo.setText(title);
        MP3Player.getInstance().setTitle(title);
    }

    public void minNextClick(ActionEvent actionEvent) {
        MP3Player.getInstance().nextSong();
        MP3Player.getInstance().play();
        String[] fullPath = MP3Player.getInstance().getPath().split("/");
        String title = fullPath[fullPath.length - 1];
        labelInfo.setText(title);
        MP3Player.getInstance().setTitle(title);
    }

    public void minPlayPauseClick(ActionEvent actionEvent) {

    }

    public void minChangeViewClick(ActionEvent actionEvent) {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/layoutMain.fxml"));
            mainMini.getChildren().setAll(box);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        if(MP3Player.getInstance().getStatus().played){
            labelInfo.setText(MP3Player.getInstance().getStatus().title);
        }
    }
}
