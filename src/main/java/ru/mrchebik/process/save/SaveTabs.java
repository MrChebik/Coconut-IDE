package ru.mrchebik.process.save;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import org.fxmisc.flowless.ScaledVirtualized;
import org.fxmisc.flowless.VirtualizedScrollPane;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.model.ExistFileToSave;

import java.nio.file.Path;

public class SaveTabs extends Thread {
    private ObservableList<Tab> tabs;

    private SaveTabs(ObservableList<Tab> tabs) {
        this.tabs = tabs;
    }

    public static SaveTabs create(ObservableList<Tab> tabs) {
        return new SaveTabs(tabs);
    }

    @Override
    public void run() {
        tabs.stream()
                .map(tab -> {
                    var path = (Path) tab.getUserData();

                    var scrollPane = (VirtualizedScrollPane) tab.getContent();
                    var scaledVirtualized = (ScaledVirtualized) scrollPane.getContent();
                    var codeArea = (CustomCodeArea) scaledVirtualized.getChildrenUnmodifiable().get(0);
                    var lines = codeArea.getText();

                    return new ExistFileToSave(lines, path);
                })
                .forEach(ExistFileToSave::save);
    }
}
