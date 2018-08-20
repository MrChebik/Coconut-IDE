package ru.mrchebik.gui.node.treeCell;

import javafx.scene.control.TreeCell;
import ru.mrchebik.injection.MenuCollector;

import java.nio.file.Path;

public class CustomTreeCell extends TreeCell<Path> {
    @Override
    public void updateItem(Path path, boolean empty) {
        super.updateItem(path, empty);

        if (empty) {
            makeEmpty(this);
        } else {
            setText(path.getFileName().toString());
            setGraphic(getTreeItem().getGraphic());
            setContextMenu(MenuCollector.contextMenu);
            setOnContextMenuRequested(event -> MenuCollector.handleRequest(event, path));
        }
    }

    private void makeEmpty(CustomTreeCell cell) {
        cell.setText(null);
        cell.setGraphic(null);
        cell.setContextMenu(null);
        cell.setStyle(null);
    }
}
