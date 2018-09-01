package ru.mrchebik.gui.place.menubar.about;

import javafx.fxml.FXML;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutPresenter {
    @FXML @SneakyThrows({ URISyntaxException.class, IOException.class })
    private void gitlab() {
        if (Desktop.isDesktopSupported())
            Desktop.getDesktop().browse(new URI("https://gitlab.com/MrChebik/Coconut-IDE"));
    }

    @FXML @SneakyThrows({ URISyntaxException.class, IOException.class })
    private void github() {
        if (Desktop.isDesktopSupported())
            Desktop.getDesktop().browse(new URI("https://github.com/MrChebik/Coconut-IDE"));
    }

    @FXML @SneakyThrows({ URISyntaxException.class, IOException.class })
    private void gpl3() {
        if (Desktop.isDesktopSupported())
            Desktop.getDesktop().browse(new URI("https://github.com/MrChebik/Coconut-IDE/blob/master/LICENSE"));
    }
}
