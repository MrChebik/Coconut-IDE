package ru.mrchebik.gui.titlebar;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

class TitlebarPresenterAction {
    void changeFullscreen() {
        TitlebarPresenter.stage.setFullScreen(!TitlebarPresenter.stage.isFullScreen());
    }

    void initStyle(MouseEvent event, String type) {
        var button = (Button) event.getSource();
        button.getStyleClass().add(type + "-pressed");
    }

    void removeStyle(MouseEvent event, String type) {
        var button = (Button) event.getSource();
        button.getStyleClass().removeAll(type + "-pressed");
    }
}
