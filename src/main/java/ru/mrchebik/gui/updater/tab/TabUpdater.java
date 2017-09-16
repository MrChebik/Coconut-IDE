package ru.mrchebik.gui.updater.tab;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import lombok.Setter;
import ru.mrchebik.actions.ReadFile;
import ru.mrchebik.gui.updater.tree.CustomTreeItem;
import ru.mrchebik.model.CustomIcons;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mrchebik on 9/3/17.
 */
@AllArgsConstructor
public class TabUpdater {
    private @Setter TabPane tabPane;

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
            } else if (!Files.isDirectory(newPath) && newPath.equals(path))
                tabs.remove(i);
        }
    }

    private Path mergeDifferences(Path path, Path newPath) {
        int start = newPath.relativize(path).getNameCount();
        int end = path.getNameCount();
        Path subPath = path.subpath(start, end);

        return newPath.resolve(subPath);
    }

    public void addObjectToTab(CustomTreeItem item) {
        String text = ReadFile.readFile(item.getValue());
        TextArea code = new TextArea(text);

        Tab tab = new Tab();
        tab.setText(item.getValue().getFileName().toString());
        CustomIcons customIcons = new CustomIcons();
        tab.setGraphic(new ImageView(customIcons.getFileImage()));
        tab.setUserData(item.getValue());
        tab.setContent(code);

        tabPane.getTabs().add(tab);
    }
}
