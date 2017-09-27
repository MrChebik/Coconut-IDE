package ru.mrchebik.gui.node.treeCell.event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.mrchebik.model.CommandPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
            if (isCut(command)) {
                Files.move(pathCommand, moveTo, StandardCopyOption.REPLACE_EXISTING);
            } else if (isCopy(command)) {
                Files.copy(pathCommand, moveTo, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private boolean isCopy(String command) {
        return "Copy".equals(command);
    }

    private boolean isCut(String command) {
        return "Cut".equals(command);
    }
}
