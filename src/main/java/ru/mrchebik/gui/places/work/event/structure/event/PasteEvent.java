package ru.mrchebik.gui.places.work.event.structure.event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.mrchebik.model.CommandPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Created by mrchebik on 9/15/17.
 */
@AllArgsConstructor
public class PasteEvent implements EventHandler<ActionEvent> {
    private CommandPath commandPath;
    private Path path;

    @Override
    @SneakyThrows(IOException.class)
    public void handle(ActionEvent event) {
        String command = commandPath.getCommand();
        Path pathCommand = commandPath.getPath();

        if (command != null) {
            Path moveTo = path.resolve(pathCommand.getFileName());
            if ("Cut".equals(command))
                Files.move(pathCommand, moveTo, StandardCopyOption.REPLACE_EXISTING);
            else if ("Copy".equals(command))
                Files.copy(pathCommand, moveTo, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
