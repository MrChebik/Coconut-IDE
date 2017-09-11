package ru.mrchebik.controller.javafx.updater.tree;

import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import ru.mrchebik.controller.javafx.updater.WatcherStructure;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.project.Project;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mrchebik on 9/3/17.
 */
public class TreeUpdater {
    private static TreeView<Path> treeView;

    @Inject
    private static Project project;

    public static void createObject(Path path, boolean isRoot) {
        if (isRoot) {
            TreeItem<Path> rootNode = setRootItem(path);

            treeView.setRoot(rootNode);
        } else {
            TreeItem<Path> parent = getItem(treeView.getRoot(), path.getParent());

            WatcherStructure watcherStructure = null;
            if (Files.isDirectory(path)) {
                watcherStructure = new WatcherStructure(path);
            }

            TreeItem<Path> newItem = new CustomTreeItem(path, watcherStructure);
            if (Files.isDirectory(path)) {
                newItem.setGraphic(new ImageView(CustomIcons.getFolderCollapseImage()));
                newItem.expandedProperty().addListener(expanderListener());
            } else {
                newItem.setGraphic(new ImageView(CustomIcons.getFileImage()));
            }

            parent.getChildren().add(newItem);
        }
    }

    public static void updateObject(Path oldPath, Path newPath) {
        TreeItem<Path> item = getItem(treeView.getRoot(), oldPath);

        item.setValue(newPath);
    }

    public static void removeObject(Path path) {
        if (project.getPath().equals(path.toString())) {
            Platform.runLater(() -> {
                treeView.setRoot(null);
            });

            return;
        }

        TreeItem<Path> parent = getItem(treeView.getRoot(), path.getParent());

        TreeItem<Path> item = getItem(parent, path);

        parent.getChildren().remove(item);
    }

    public static void setRootToTreeView() {
        Path projectPath = project.getPath();

        WatcherStructure rootOutWatcher = new WatcherStructure(projectPath.getParent());
        rootOutWatcher.start();

        TreeItem<Path> rootNode = setRootItem(projectPath);

        treeView.setRoot(rootNode);
    }

    public static TreeItem<Path> getItem(TreeItem<Path> root, Path path) {
        if (root.getValue().equals(path)) {
            return root;
        }

        for (TreeItem<Path> child : root.getChildren()) {
            if (!path.equals(child.getValue())) {
                if (!child.getChildren().isEmpty()) {
                    TreeItem<Path> item = getItem(child, path);
                    if (item != null) {
                        return item;
                    }
                }
            } else {
                return child;
            }
        }

        return null;
    }

    static ChangeListener<Boolean> expanderListener() {
        return (observable, oldValue, newValue) -> {
            BooleanProperty bb = (BooleanProperty) observable;

            TreeItem t = (TreeItem) bb.getBean();

            t.setGraphic(new ImageView(newValue ? CustomIcons.getFolderExpandImage() : CustomIcons.getFolderCollapseImage()));
        };
    }

    private static TreeItem<Path> setRootItem(Path path) {
        WatcherStructure rootInWatcher = new WatcherStructure(path);

        TreeItem<Path> rootNode = new CustomTreeItem(path, rootInWatcher);
        rootNode.setExpanded(true);
        rootNode.setGraphic(new ImageView(CustomIcons.getFolderExpandImage()));
        rootNode.expandedProperty().addListener(expanderListener());

        return rootNode;
    }

    public static void setTreeView(TreeView<Path> treeView) {
        TreeUpdater.treeView = treeView;
    }
}
