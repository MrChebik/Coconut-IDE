package ru.mrchebik.view.treeview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Created by mrchebik on 8/30/17.
 */
public class FilePathTreeItem extends TreeItem<Path> {
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;

    public boolean isDirectory() {
        return Files.isDirectory(getValue());
    }

    public FilePathTreeItem(Path f) {
        super(f);
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
                        .map(FilePathTreeItem::new)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            } catch (IOException e) {
                e.printStackTrace();
                return FXCollections.emptyObservableList();
            }
        }

        return FXCollections.emptyObservableList();
    }
}
