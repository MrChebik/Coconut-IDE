package ru.mrchebik.gui.updater;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import ru.mrchebik.gui.node.CustomTreeItem;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.cell.HighlightCell;
import ru.mrchebik.project.Project;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

@AllArgsConstructor
public class TreeUpdater {
    private Project project;
    private TreeView<Path> treeView;
    private TabUpdater tabUpdater;

    void createObject(Path path, boolean isRoot) {
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
            if (Files.isDirectory(path)) {
                newItem.setGraphic(new ImageView(Icons.FOLDER_COLLAPSE.get()));
                newItem.expandedProperty().addListener(expanderListener());
            } else
                newItem.setGraphic(new ImageView(Icons.FILE.get()));

            parent.getChildren().add(newItem);

            sortItemsInTree(parent);
        }
    }

    public ChangeListener<Boolean> expanderListener() {
        return (observable, oldValue, newValue) -> {
            BooleanProperty bb = (BooleanProperty) observable;

            TreeItem t = (TreeItem) bb.getBean();

            t.setGraphic(new ImageView((newValue ? Icons.FOLDER_EXPAND : Icons.FOLDER_COLLAPSE).get()));

            if (newValue)
                scheduleHighlight();
        };
    }

    public static TreeItem<Path> getItem(TreeItem<Path> root, Path path) {
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

    void removeObject(Path path) {
        if (Project.path.equals(path)) {
            Platform.runLater(() -> {
                treeView.setRoot(null);
            });
        } else {
            TreeItem<Path> parent = getItem(treeView.getRoot(), path.getParent());
            TreeItem<Path> item = getItem(parent, path);
            parent.getChildren().remove(item);
        }
    }

    private TreeItem<Path> setRootItem(Path path) {
        WatcherStructure rootInWatcher = new WatcherStructure(path, project, tabUpdater, this);

        TreeItem<Path> rootNode = new CustomTreeItem(path, rootInWatcher, project, tabUpdater, this);
        rootNode.setExpanded(true);
        rootNode.setGraphic(new ImageView(Icons.FOLDER_EXPAND.get()));
        rootNode.expandedProperty().addListener(expanderListener());

        return rootNode;
    }

    public void setRootToTreeView() {
        WatcherStructure rootOutWatcher = new WatcherStructure(Project.path.getParent(), project, tabUpdater, this);
        rootOutWatcher.start();

        TreeItem<Path> rootNode = setRootItem(Project.path);

        treeView.setRoot(rootNode);
    }

    private void scheduleHighlight() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                HighlightCell.highlight();
            }
        };
        timer.schedule(timerTask, 50);
    }

    private void sortItemsInTree(TreeItem<Path> start) {
        if (!treeView.getRoot().equals(start))
            start.getChildren().sort(Comparator.comparing(e -> e.getValue().toString()));
    }

    void updateObject(Path oldPath, Path newPath) {
        TreeItem<Path> item = getItem(treeView.getRoot(), oldPath);
        item.setValue(newPath);

        sortItemsInTree(item.getParent());
    }
}
