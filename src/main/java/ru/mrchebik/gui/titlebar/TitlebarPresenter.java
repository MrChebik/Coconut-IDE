package ru.mrchebik.gui.titlebar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.mrchebik.screen.measurement.Point;

public class TitlebarPresenter {
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
    private void doTitleHide() {
        stage.setIconified(true);
    }

    @FXML
    private void doTitleFull() {
        changeFullscreen();
    }

    @FXML
    private void doTitleClose() {
        stage.close();
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

    private void changeFullscreen() {
        stage.setFullScreen(!stage.isFullScreen());
    }
}
