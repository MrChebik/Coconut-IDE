package ru.mrchebik.controller.actions.autosave.saver;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import ru.mrchebik.controller.actions.autosave.Autosave;
import ru.mrchebik.model.controller.actions.autosave.ExistFileToSave;

import java.nio.file.Path;

/**
 * Created by mrchebik on 9/2/17.
 */
public class SaveTabs extends Autosave {
    private ObservableList<Tab> tabs;

    public SaveTabs(ObservableList<Tab> tabs) {
        this.tabs = tabs;
    }

    @Override
    public void save() {
        tabs.stream()
                .map(tab -> {
                    Path path = (Path) tab.getUserData();

                    TextArea textArea = (TextArea) tab.getContent();
                    String lines = textArea.getText();

                    return new ExistFileToSave(path, lines);
                })
                .map(SaveFile::new)
                .forEach(Thread::start);
    }
}
