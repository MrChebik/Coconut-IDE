package ru.mrchebik.gui.titlebar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.mrchebik.screen.measurement.Point;

import java.net.URL;
import java.util.ResourceBundle;

public class TitlebarPresenter extends TitlebarPresenterAction implements Initializable {
    public static Stage stage;
    public static Point point;

    static {
        point = new Point();
    }

    @FXML
    public AnchorPane titlebar;
    @FXML
    public Label title;
    @FXML
    public Button green;
    @FXML
    public Button orange;
    @FXML
    public Button red;

    @FXML
    private void doTitleHide() {
        stage.setIconified(true);
    }

    @FXML
    private void pressedTitleHide(MouseEvent event) {
        initStyle(event,"green");
    }

    @FXML
    private void releasedTitleHide(MouseEvent event) {
        removeStyle(event, "green");
    }

    @FXML
    private void doTitleFull() {
        changeFullscreen();
    }

    @FXML
    private void pressedTitleFull(MouseEvent event) {
        initStyle(event,"orange");
    }

    @FXML
    private void releasedTitleFull(MouseEvent event) {
        removeStyle(event, "orange");
    }

    @FXML
    private void doTitleClose() {
        stage.close();
    }

    @FXML
    private void pressedTitleClose(MouseEvent event) {
        initStyle(event, "red");
    }

    @FXML
    private void releasedTitleClose(MouseEvent event) {
        removeStyle(event, "red");
    }

    @FXML
    private void mousePressed(MouseEvent event) {
        point.x = stage.getX() - event.getScreenX();
        point.y = stage.getY() - event.getScreenY();
    }

    @FXML
    private void mouseDragged(MouseEvent event) {
        stage.setX(event.getScreenX() + point.x);
        stage.setY(event.getScreenY() + point.y);
    }

    @FXML
    private void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2)
            changeFullscreen();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        green.getStyleClass().removeAll("button");
        orange.getStyleClass().removeAll("button");
        red.getStyleClass().removeAll("button");
    }
}
