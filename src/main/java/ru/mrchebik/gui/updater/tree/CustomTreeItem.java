package ru.mrchebik.gui.updater.tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import ru.mrchebik.gui.updater.WatcherStructure;
import ru.mrchebik.gui.updater.tab.TabUpdater;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Created by mrchebik on 8/30/17.
 */
public class CustomTreeItem extends TreeItem<Path> {
    private Project project;
    private TabUpdater tabUpdater;
    private TreeUpdater treeUpdater;

    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;

    public boolean isDirectory() {
        return Files.isDirectory(getValue());
    }

    CustomTreeItem(Path f, WatcherStructure watcherStructure, Project project, TabUpdater tabUpdater, TreeUpdater treeUpdater) {
        super(f);
        if (watcherStructure != null) {
            watcherStructure.start();
        }
        this.project = project;
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
                            watcherStructure = new WatcherStructure(e, project, tabUpdater, treeUpdater);

                        CustomTreeItem item = new CustomTreeItem(e, watcherStructure, project, tabUpdater, treeUpdater);

                        CustomIcons customIcons = new CustomIcons();
                        item.setGraphic(new ImageView(item.isDirectory() ? customIcons.getFolderCollapseImage() : customIcons.getFileImage()));
                        if (isDirectory())
                            item.expandedProperty().addListener(treeUpdater.expanderListener());

                        return item;
                    })
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        return FXCollections.emptyObservableList();
    }
}