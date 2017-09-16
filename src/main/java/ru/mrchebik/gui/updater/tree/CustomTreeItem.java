package ru.mrchebik.gui.updater.tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import ru.mrchebik.gui.updater.WatcherStructure;
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
    private TreeView<Path> treeView;
    private TabPane tabPane;

    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;

    public boolean isDirectory() {
        return Files.isDirectory(getValue());
    }

    CustomTreeItem(Path f, WatcherStructure watcherStructure, Project project, TreeView<Path> treeView, TabPane tabPane) {
        super(f);
        if (watcherStructure != null) {
            watcherStructure.start();
        }
        this.project = project;
        this.treeView = treeView;
        this.tabPane = tabPane;
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
                            watcherStructure = new WatcherStructure(e, project, tabPane, treeView);

                        CustomTreeItem item = new CustomTreeItem(e, watcherStructure, project, treeView, tabPane);

                        CustomIcons customIcons = new CustomIcons();
                        item.setGraphic(new ImageView(item.isDirectory() ? customIcons.getFolderCollapseImage() : customIcons.getFileImage()));
                        if (isDirectory()) {
                            TreeUpdater treeUpdater = new TreeUpdater(project, tabPane, treeView);
                            item.expandedProperty().addListener(treeUpdater.expanderListener());
                        }

                        return item;
                    })
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        return FXCollections.emptyObservableList();
    }
}
