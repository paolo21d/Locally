package pckLocally;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ControllerMini {

    public VBox mainMini;

    public void but1Click(ActionEvent actionEvent) {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/layoutMain.fxml"));

            mainMini.getChildren().setAll(box);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
