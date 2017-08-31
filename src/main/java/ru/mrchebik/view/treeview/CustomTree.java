package ru.mrchebik.view.treeview;

import javafx.scene.control.TreeView;

import java.nio.file.Path;

/**
 * Created by mrchebik on 8/31/17.
 */
public class CustomTree extends TreeView<Path> implements Cloneable {
    public TreeView<Path> view;

    public CustomTree(TreeView<Path> view) {
        super();

        this.view = view;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
