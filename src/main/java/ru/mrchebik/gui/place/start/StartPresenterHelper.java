package ru.mrchebik.gui.place.start;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ru.mrchebik.call.startup.CallStartupWrapper;
import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.locale.Locale;

class StartPresenterHelper {
    static void initLocale(Button  button,
                           Tooltip tooltip) {
        button.setText(Locale.NEW_PROJECT);
        tooltip.setText(Locale.SETUP_HOME_TOOLTIP);
    }

    static void initNewProject(Button         button,
                               StartupWrapper startup) {
        button.setDisable(startup.isCorrectHome());
    }

    static void initAnimation(ImageView image) {
        var scaleTransition = new ScaleTransition(Duration.millis(2000), image);
        scaleTransition.setToX(1.1f);
        scaleTransition.setToY(1.1f);
        scaleTransition.setCycleCount(Timeline.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    static void setHomeAndEnable(Button             button,
                                 CallStartupWrapper callStartup,
                                 StartPlace         place,
                                 StartupWrapper     startup) {
        callStartup.callSetupHome(startup, place);

        boolean isCorrect = startup.isCorrectHome();
        button.setDisable(isCorrect);
    }
}
