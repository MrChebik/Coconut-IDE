package ru.mrchebik.actions.autosave.saver;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import ru.mrchebik.model.controller.actions.autosave.ExistFileToSave;

import java.nio.file.Path;

/**
 * Created by mrchebik on 9/2/17.
 */
@AllArgsConstructor
public class SaveTabs extends Thread {
    private ObservableList<Tab> tabs;

    @Override
    public void run() {
        tabs.stream()
                .map(tab -> {
                    Path path = (Path) tab.getUserData();

                    TextArea textArea = (TextArea) tab.getContent();
                    String lines = textArea.getText();

                    return new ExistFileToSave(path, lines);
                })
                .forEach(ExistFileToSave::save);
    }
}
