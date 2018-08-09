package ru.mrchebik.gui.node;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import ru.mrchebik.gui.updater.TabUpdater;
import ru.mrchebik.gui.updater.TreeUpdater;
import ru.mrchebik.gui.updater.WatcherStructure;
import ru.mrchebik.icons.Icons;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class CustomTreeItem extends TreeItem<Path> {
    private TabUpdater tabUpdater;
    private TreeUpdater treeUpdater;

    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;

    public boolean isDirectory() {
        return Files.isDirectory(getValue());
    }

    public CustomTreeItem(Path f, WatcherStructure watcherStructure, TabUpdater tabUpdater, TreeUpdater treeUpdater) {
        super(f);
        if (watcherStructure != null) {
            watcherStructure.start();
        }
        this.tabUpdater = tabUpdater;
        this.treeUpdater = treeUpdater;
    }

    @Override
    public ObservableList<TreeItem<Path>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren());
        }

        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            isLeaf = Files.exists(getValue()) && !Files.isDirectory(getValue());
        }
        return isLeaf;
    }

    @SneakyThrows(IOException.class)
    private ObservableList<TreeItem<Path>> buildChildren() {
        if (Files.isDirectory(getValue())) {
            return Files.list(getValue())
                    .map(e -> {
                        WatcherStructure watcherStructure = null;
                        if (Files.isDirectory(e))
                            watcherStructure = new WatcherStructure(e, tabUpdater, treeUpdater);

                        CustomTreeItem item = new CustomTreeItem(e, watcherStructure, tabUpdater, treeUpdater);

                        item.setGraphic(new ImageView((item.isDirectory() ? Icons.FOLDER_COLLAPSE : Icons.FILE).get()));
                        if (isDirectory())
                            item.expandedProperty().addListener(treeUpdater.expanderListener());

                        return item;
                    })
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        return FXCollections.emptyObservableList();
    }
}
