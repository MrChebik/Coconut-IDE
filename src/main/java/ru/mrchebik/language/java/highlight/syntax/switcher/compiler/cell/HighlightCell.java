package ru.mrchebik.language.java.highlight.syntax.switcher.compiler.cell;

import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import ru.mrchebik.gui.collector.ComponentsCollector;
import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.JavaCompilerSyntax;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class HighlightCell extends JavaCompilerSyntax {
    private static final String ERROR = "-fx-background-color: #cc1b00; -fx-text-fill: white";

    private static List<TreeItem<Path>> treeItems;
    private static List<Node> cells;

    private static List<TreeItem<Path>> getAllTreeItems(TreeItem<Path> root) {
        List<TreeItem<Path>> treeItems = new ArrayList<>();
        if (root.equals(ComponentsCollector.treeView.getRoot()))
            treeItems.add(root);

        for (TreeItem<Path> child : root.getChildren()) {
            treeItems.add(child);
            if (!child.getChildren().isEmpty())
                treeItems.addAll(getAllTreeItems(child));
        }

        return treeItems;
    }

    public static void highlight() {
        if (diagnostics != null) {
            Set<Node> treeCells = ComponentsCollector.treeView.lookupAll(".tree-cell");
            cells = new ArrayList<>(treeCells);
            cells = cells.parallelStream().filter(c -> ((TreeCell) c).getTreeItem() != null).collect(Collectors.toList());
            treeItems = getAllTreeItems(ComponentsCollector.treeView.getRoot());

            diagnostics.stream()
                    .filter(JavaCompilerSyntax::isErrorKind)
                    .map(HighlightCell::isCurrTreeItem)
                    .filter(Objects::nonNull)
                    .forEach(HighlightCell::highlightCells);

            cells.parallelStream()
                    .forEach(c -> c.setStyle(null));
        }
    }

    private static TreeItem<Path> isCurrTreeItem(Diagnostic diagnostic) {
        JavaFileObject javaFileObject = (JavaFileObject) diagnostic.getSource();
        Optional<TreeItem<Path>> item = treeItems.parallelStream()
                .filter(c -> javaFileObject.getName().equals(c.getValue().toString()))
                .findFirst();

        if (item.isPresent()) {
            TreeItem<Path> treeItem = item.get();
            treeItems.remove(treeItem);
            return treeItem;
        }

        return null;
    }

    private static void highlightCells(TreeItem treeItem) {
        Optional<Node> item = cells.parallelStream()
                .filter(c -> {
                    TreeCell treeCell = (TreeCell) c;
                    return treeItem.equals(treeCell.getTreeItem());
                })
                .findFirst();

        if (item.isPresent()) {
            TreeCell treeCell = (TreeCell) item.get();
            treeCell.setStyle(ERROR);
            cells.remove(treeCell);
        }
    }
}
