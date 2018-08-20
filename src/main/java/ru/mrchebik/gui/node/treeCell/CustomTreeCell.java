package ru.mrchebik.gui.node.treeCell;

import javafx.scene.control.TreeCell;
import ru.mrchebik.gui.collector.contextmenu.treeview.MenuTreeviewCollector;

import java.nio.file.Path;

public class CustomTreeCell extends TreeCell<Path> {
    @Override
    public void updateItem(Path path, boolean empty) {
        super.updateItem(path, empty);

        if (empty)
            makeEmpty();
        else {
            setText(path.getFileName().toString());
            setGraphic(getTreeItem().getGraphic());
            setContextMenu(MenuTreeviewCollector.contextMenu);
            setOnContextMenuRequested(event -> MenuTreeviewCollector.handleRequest(event, path));
        }
    }

    private void makeEmpty() {
        this.setText(null);
        this.setGraphic(null);
        this.setContextMenu(null);
        this.setOnContextMenuRequested(null);
        this.setStyle(null);
    }
}
