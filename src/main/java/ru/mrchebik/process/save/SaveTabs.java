package ru.mrchebik.process.save;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import ru.mrchebik.algorithm.AlgorithmGui;
import ru.mrchebik.model.ExistFileToSave;

import java.nio.file.Path;

public class SaveTabs extends Thread {
    private ObservableList<Tab> tabs;

    public SaveTabs(ObservableList<Tab> tabs) {
        this.tabs = tabs;
    }

    @Override
    public void run() {
        tabs.stream()
                .map(tab -> {
                    var codeArea = AlgorithmGui.getCodeAreaByTab(tab);

                    var text = codeArea.getText();
                    var path = (Path) tab.getUserData();

                    return new ExistFileToSave(text, path);
                })
                .forEach(ExistFileToSave::save);
    }
}
