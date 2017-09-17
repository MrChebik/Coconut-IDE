package ru.mrchebik.gui.updater.tab;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.fxmisc.flowless.VirtualizedScrollPane;
import ru.mrchebik.gui.updater.tree.CustomTreeItem;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.syntax.Highlight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Created by mrchebik on 9/3/17.
 */
@AllArgsConstructor
public class TabUpdater {
    private TabPane tabPane;
    private Highlight highlight;

    public void updateTabs(Path newPath, Path toRename) {
        ObservableList<Tab> tabs = tabPane.getTabs();

        for (int i = 0; i < tabs.size(); i++) {
            Path path = (Path) tabs.get(i).getUserData();

            if (Files.isDirectory(newPath) && path.startsWith(newPath.getParent())) {
                Path newPathRecord = mergeDifferences(path, newPath);
                tabs.get(i).setUserData(newPathRecord);
            } else if (!Files.isDirectory(newPath) && path.equals(toRename)) {
                Tab tab = tabs.get(i);
                Platform.runLater(() -> tab.setText(newPath.getFileName().toString()));
                tab.setUserData(newPath);
            }
        }
    }

    public void deleteTab(Path pathToDelete) {
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

    private Path mergeDifferences(Path path, Path newPath) {
        int start = newPath.relativize(path).getNameCount();
        int end = path.getNameCount();
        Path subPath = path.subpath(start, end);

        return newPath.resolve(subPath);
    }

    public void addObjectToTab(CustomTreeItem item) {
        String text = getText(item.getValue());
        CodePlace codePlace = new CodePlace(text, highlight);

        Tab tab = new Tab();
        tab.setText(item.getValue().getFileName().toString());
        CustomIcons customIcons = new CustomIcons();
        tab.setGraphic(new ImageView(customIcons.getFileImage()));
        tab.setUserData(item.getValue());

        VirtualizedScrollPane scrollPane = new VirtualizedScrollPane<>(codePlace);
        tab.setContent(scrollPane);

        tabPane.getTabs().add(tab);
    }

    @SneakyThrows(IOException.class)
    private String getText(Path path) {
        return Files.readAllLines(path).stream()
                .collect(Collectors.joining("\n"));
    }
}
