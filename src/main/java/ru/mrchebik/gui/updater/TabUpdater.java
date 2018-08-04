package ru.mrchebik.gui.updater;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.fxmisc.flowless.VirtualizedScrollPane;
import ru.mrchebik.gui.node.CustomTreeItem;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.highlight.Highlight;
import ru.mrchebik.highlight.syntax.Syntax;
import ru.mrchebik.highlight.syntax.switcher.javaCompiler.tab.HighlightTab;
import ru.mrchebik.model.CustomIcons;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TabUpdater {
    private TabPane tabPane;
    private Highlight highlight;
    private Syntax syntax;
    private Stage stage;

    public void addObjectToTab(CustomTreeItem item) {
        String text = getText(item.getValue());
        CustomCodeArea customCodeArea = new CustomCodeArea(text, highlight, syntax, stage, item.getValue().getFileName().toString());

        Tab tab = new Tab();
        tab.setText(item.getValue().getFileName().toString());
        CustomIcons customIcons = new CustomIcons();
        tab.setGraphic(new ImageView(customIcons.getFileImage()));
        tab.setUserData(item.getValue());

        VirtualizedScrollPane scrollPane = new VirtualizedScrollPane<>(customCodeArea);
        tab.setContent(scrollPane);

        tabPane.getTabs().add(tab);

        focusToTab();

        scheduleHighlight();
    }

    private void focusToTab() {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.selectLast();
    }

    void deleteTab(Path pathToDelete) {
        ObservableList<Tab> tabs = tabPane.getTabs();

        for (int i = 0; i < tabs.size(); i++) {
            Path path = (Path) tabs.get(i).getUserData();

            if (!Files.isDirectory(path) && pathToDelete.equals(path)) {
                int finalI = i;
                Platform.runLater(() -> tabs.remove(finalI));

                break;
            }
        }
    }

    @SneakyThrows(IOException.class)
    private String getText(Path path) {
        return Files.readAllLines(path).stream()
                .collect(Collectors.joining("\n"));
    }

    private Path mergeDifferences(Path path, Path newPath) {
        int start = newPath.relativize(path).getNameCount();
        int end = path.getNameCount();
        Path subPath = path.subpath(start, end);

        return newPath.resolve(subPath);
    }

    private void scheduleHighlight() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                HighlightTab.highlight();
            }
        };
        timer.schedule(timerTask, 50);
    }

    void updateTabs(Path newPath, Path toRename) {
        ObservableList<Tab> tabs = tabPane.getTabs();

        for (Tab tab : tabs) {
            Path path = (Path) tab.getUserData();

            if (Files.isDirectory(newPath) && path.startsWith(newPath.getParent())) {
                Path newPathRecord = mergeDifferences(path, newPath);
                tab.setUserData(newPathRecord);
            } else if (!Files.isDirectory(newPath) && path.equals(toRename)) {
                Platform.runLater(() -> tab.setText(newPath.getFileName().toString()));
                tab.setUserData(newPath);

                ((CustomCodeArea) ((VirtualizedScrollPane) tab.getContent()).getContent()).setName(newPath.getFileName().toString());
            }
        }
    }
}
