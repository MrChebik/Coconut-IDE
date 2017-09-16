package ru.mrchebik.gui.updater.tree;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import ru.mrchebik.gui.updater.WatcherStructure;
import ru.mrchebik.gui.updater.tab.TabUpdater;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.Project;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mrchebik on 9/3/17.
 */
@AllArgsConstructor
public class TreeUpdater {
    private Project project;
    private TreeView<Path> treeView;
    private TabUpdater tabUpdater;

    public void createObject(Path path, boolean isRoot) {
        if (isRoot) {
            TreeItem<Path> rootNode = setRootItem(path);
            treeView.setRoot(rootNode);
        } else {
            TreeItem<Path> parent = getItem(treeView.getRoot(), path.getParent());

            WatcherStructure watcherStructure = null;
            if (Files.isDirectory(path)) {
                watcherStructure = new WatcherStructure(path, project, tabUpdater, this);
            }

            TreeItem<Path> newItem = new CustomTreeItem(path, watcherStructure, project, tabUpdater, this);
            CustomIcons customIcons = new CustomIcons();
            if (Files.isDirectory(path)) {
                newItem.setGraphic(new ImageView(customIcons.getFolderCollapseImage()));
                newItem.expandedProperty().addListener(expanderListener());
            } else {
                newItem.setGraphic(new ImageView(customIcons.getFileImage()));
            }

            parent.getChildren().add(newItem);
        }
    }

    public void updateObject(Path oldPath, Path newPath) {
        TreeItem<Path> item = getItem(treeView.getRoot(), oldPath);
        item.setValue(newPath);
    }

    public void removeObject(Path path) {
        if (project.getPath().equals(path)) {
            Platform.runLater(() -> {
                treeView.setRoot(null);
            });
        } else {
            TreeItem<Path> parent = getItem(treeView.getRoot(), path.getParent());
            TreeItem<Path> item = getItem(parent, path);
            parent.getChildren().remove(item);
        }
    }

    public void setRootToTreeView() {
        Path projectPath = project.getPath();

        WatcherStructure rootOutWatcher = new WatcherStructure(projectPath.getParent(), project, tabUpdater, this);
        rootOutWatcher.start();

        TreeItem<Path> rootNode = setRootItem(projectPath);

        treeView.setRoot(rootNode);
    }

    public TreeItem<Path> getItem(TreeItem<Path> root, Path path) {
        if (root.getValue().equals(path))
            return root;

        for (TreeItem<Path> child : root.getChildren()) {
            if (!path.equals(child.getValue())) {
                if (!child.getChildren().isEmpty()) {
                    TreeItem<Path> item = getItem(child, path);
                    if (item != null)
                        return item;
                }
            } else
                return child;
        }

        return null;
    }

    ChangeListener<Boolean> expanderListener() {
        return (observable, oldValue, newValue) -> {
            BooleanProperty bb = (BooleanProperty) observable;

            TreeItem t = (TreeItem) bb.getBean();

            CustomIcons customIcons = new CustomIcons();
            t.setGraphic(new ImageView(newValue ? customIcons.getFolderExpandImage() : customIcons.getFolderCollapseImage()));
        };
    }

    private TreeItem<Path> setRootItem(Path path) {
        WatcherStructure rootInWatcher = new WatcherStructure(path, project, tabUpdater, this);

        TreeItem<Path> rootNode = new CustomTreeItem(path, rootInWatcher, project, tabUpdater, this);
        rootNode.setExpanded(true);
        CustomIcons customIcons = new CustomIcons();
        rootNode.setGraphic(new ImageView(customIcons.getFolderExpandImage()));
        rootNode.expandedProperty().addListener(expanderListener());

        return rootNode;
    }
}
