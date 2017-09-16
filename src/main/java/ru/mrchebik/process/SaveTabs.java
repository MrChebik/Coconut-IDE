package ru.mrchebik.process;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import lombok.AllArgsConstructor;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.model.ExistFileToSave;

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

                    VirtualizedScrollPane scrollPane = (VirtualizedScrollPane) tab.getContent();
                    CodeArea codeArea = (CodeArea) scrollPane.getContent();
                    String lines = codeArea.getText();

                    return new ExistFileToSave(path, lines);
                })
                .forEach(ExistFileToSave::save);
    }
}
