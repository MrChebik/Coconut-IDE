package ru.mrchebik.language.java.highlight.syntax.switcher.javaCompiler.cell;

import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.language.java.highlight.syntax.switcher.javaCompiler.JavaCompilerSyntax;
import ru.mrchebik.process.save.SaveTabsProcess;
import ru.mrchebik.project.Project;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class HighlightCell extends JavaCompilerSyntax {
    private static final String ERROR = "-fx-background-color: #cc1b00; -fx-text-fill: white";

    private static List<TreeItem<Path>> treeItems;
    private static List<Node> cells;

    public HighlightCell(CustomCodeArea customCodeArea, Project project, SaveTabsProcess saveTabsProcess, TabPane tabPane, TreeView<Path> treeView) {
        super(customCodeArea, project, saveTabsProcess, tabPane, treeView);
    }

    private static List<TreeItem<Path>> getAllTreeItems(TreeItem<Path> root) {
        List<TreeItem<Path>> treeItems = new ArrayList<>();
        if (root.equals(treeView.getRoot())) {
            treeItems.add(root);
        }

        for (TreeItem<Path> child : root.getChildren()) {
            treeItems.add(child);
            if (!child.getChildren().isEmpty()) {
                treeItems.addAll(getAllTreeItems(child));
            }
        }

        return treeItems;
    }

    public static void highlight() {
        if (diagnostics != null) {
            Set<Node> treeCells = treeView.lookupAll(".tree-cell");
            cells = new ArrayList<>(treeCells);
            cells = cells.parallelStream().filter(c -> ((TreeCell) c).getTreeItem() != null).collect(Collectors.toList());
            treeItems = getAllTreeItems(treeView.getRoot());

            diagnostics.stream()
                    .filter(JavaCompilerSyntax::isErrorKind)
                    .map(HighlightCell::isCurrTreeItem)
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

    public static void highlightCells(TreeItem treeItem) {
        Optional<Node> item = cells.parallelStream()
                .filter(c -> {
                    TreeCell treeCell = (TreeCell) c;
                    return treeCell.getTreeItem().equals(treeItem);
                })
                .findFirst();

        if (item.isPresent()) {
            TreeCell treeCell = (TreeCell) item.get();
            treeCell.setStyle(ERROR);
            cells.remove(treeCell);
        }
    }
}
