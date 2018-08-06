package ru.mrchebik.process.save;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import org.fxmisc.flowless.ScaledVirtualized;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.model.ExistFileToSave;

import java.nio.file.Path;

/**
 * Created by mrchebik on 9/2/17.
 */
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
                    Path path = (Path) tab.getUserData();

                    VirtualizedScrollPane scrollPane = (VirtualizedScrollPane) tab.getContent();
                    ScaledVirtualized<CodeArea> scaledVirtualized = (ScaledVirtualized) scrollPane.getContent();
                    CustomCodeArea codeArea = (CustomCodeArea) scaledVirtualized.getChildrenUnmodifiable().get(0);
                    String lines = codeArea.getText();

                    return new ExistFileToSave(lines, path);
                })
                .forEach(ExistFileToSave::save);
    }
}
