package ru.mrchebik.controller.javafx.updater.tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import ru.mrchebik.controller.javafx.updater.WatcherStructure;
import ru.mrchebik.model.CustomIcons;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Created by mrchebik on 8/30/17.
 */
public class CustomTreeItem extends TreeItem<Path> {
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;

    private WatcherStructure watcherStructure;

    public boolean isDirectory() {
        return Files.isDirectory(getValue());
    }

    public CustomTreeItem(Path f, WatcherStructure watcherStructure) {
        super(f);
        if (watcherStructure != null) {
            this.watcherStructure = watcherStructure;
            watcherStructure.start();
        }
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

    private ObservableList<TreeItem<Path>> buildChildren() {
        if (Files.isDirectory(getValue())) {
            try {
                return Files.list(getValue())
                        .map(e -> {
                            WatcherStructure watcherStructure = null;

                            if (Files.isDirectory(e)) {
                                watcherStructure = new WatcherStructure(e);
                            }

                            CustomTreeItem item = new CustomTreeItem(e, watcherStructure);

                            item.setGraphic(new ImageView(item.isDirectory() ? CustomIcons.getFolderCollapseImage() : CustomIcons.getFileImage()));
                            if (isDirectory()) {
                                item.expandedProperty().addListener(TreeUpdater.expanderListener());
                            }

                            return item;
                        })
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            } catch (IOException e) {
                e.printStackTrace();
                return FXCollections.emptyObservableList();
            }
        }

        return FXCollections.emptyObservableList();
    }
}
