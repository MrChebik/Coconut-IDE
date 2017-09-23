package ru.mrchebik.syntax;

import com.sun.tools.javac.file.BaseFileObject;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import ru.mrchebik.gui.node.CustomCodeArea;
import ru.mrchebik.model.Project;
import ru.mrchebik.process.SaveTabsProcess;

import javax.tools.*;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class ErrorProcessSyntax extends Thread {
    private static final String TRANSPARENT = "close-color: white;";
    private static final String ERROR = "-fx-background-color: #cc1b00; -fx-text-base-color: white; close-color: black; -fx-text-fill: white";

    private CustomCodeArea customCodeArea;
    private Project project;
    private SaveTabsProcess saveTabsProcess;
    private static TabPane tabPane;
    private static TreeView<Path> treeView;

    private static DiagnosticCollector<JavaFileObject> diagnostics;

    ErrorProcessSyntax(CustomCodeArea customCodeArea,
                       Project project,
                       SaveTabsProcess saveTabsProcess,
                       TabPane tabPane,
                       TreeView<Path> treeView) {
        this.customCodeArea = customCodeArea;
        this.project = project;
        this.saveTabsProcess = saveTabsProcess;
        ErrorProcessSyntax.tabPane = tabPane;
        ErrorProcessSyntax.treeView = treeView;
    }

    @Override
    public void run() {
        compile();
    }

    private void compile() {
        saveTabsProcess.runSynch();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), null);

        String[] compileOptions = new String[]{"-d", project.getPathOut().toString()};
        Iterable<String> compilationOptions = Arrays.asList(compileOptions);

        List<JavaFileObject> javaObjects = scanRecursivelyForJavaObjects(project.getPathSource().toFile(), fileManager);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, compilationOptions, null, javaObjects);

        task.call();
        highlightCells();
        highlightTabs();
        computeProblems();

        Platform.runLater(() -> customCodeArea.setStyleSpans(0, customCodeArea.getCodeAreaCSS().getStyleSpans(0, customCodeArea.getText().length())));
    }

    private void computeProblems() {
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            //System.out.printPlatform.runLater(area::requestFocus);ln(diagnostic.getKind());
            //System.out.println(diagnostic.getStartPosition());
            //System.out.println(diagnostic.getEndPosition());
            //System.out.println(diagnostic.getSource());
            //System.out.println(diagnostic.getMessage(null));

            BaseFileObject baseFileObject = (BaseFileObject) diagnostic.getSource();
            if (baseFileObject.getShortName().equals(customCodeArea.getName()) && diagnostic.getStartPosition() > -1) {
                int start = (int) diagnostic.getStartPosition();
                int end = (int) diagnostic.getEndPosition();

                if (start == end) {
                    if (start > 0) {
                        start -= 1;
                    }
                    if (end < customCodeArea.getText().length() - 1) {
                        end += 1;
                    }
                } else if (end < start) {
                    end = start + 1;
                    start--;
                    if (end == customCodeArea.getText().length() - 1) {
                        end--;
                    }
                }

                customCodeArea.getCodeAreaCSS().setStyleClass(start, end, "error");
            }
        }
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

    public static void highlightCells() {
        if (diagnostics != null) {
            Set<Node> treeCells = treeView.lookupAll(".tree-cell");
            List<Node> cells = new ArrayList<>(treeCells);

            List<TreeItem<Path>> treeItems = getAllTreeItems(treeView.getRoot());
            treeItems.forEach(item -> {
                Path path = item.getValue();
                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                    BaseFileObject baseFileObject = (BaseFileObject) diagnostic.getSource();
                    if (baseFileObject.getName().equals(path.toString())) {
                        for (Node node :  cells) {
                            TreeCell treeCell = (TreeCell) node;
                            if (treeCell.getTreeItem() != null && treeCell.getTreeItem().equals(item)) {
                                treeCell.setStyle(ERROR);
                                break;
                            }
                        }
                        return;
                    }
                }

                for (Node node :  cells) {
                    TreeCell treeCell = (TreeCell) node;
                    if (treeCell.getTreeItem() != null) {
                        if (treeCell.getTreeItem().equals(item)) {
                            treeCell.setStyle(null);
                            break;
                        }
                    }
                }
            });
        }
    }

    public static void highlightTabs() {
        if (diagnostics != null) {
            tabPane.getTabs().forEach(t -> {
                Path path = (Path) t.getUserData();
                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                    BaseFileObject baseFileObject = (BaseFileObject) diagnostic.getSource();
                    if (baseFileObject.getName().equals(path.toString())) {
                        t.setStyle(ERROR);
                        return;
                    }
                }

                t.setStyle(TRANSPARENT);
            });
        }
    }

    private List<JavaFileObject> scanRecursivelyForJavaObjects(File dir, StandardJavaFileManager fileManager) {
        List<JavaFileObject> javaObjects = new LinkedList<>();
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                javaObjects.addAll(scanRecursivelyForJavaObjects(file, fileManager));
            } else if (file.getName().toLowerCase().endsWith(".java")) {
                javaObjects.add(fileManager.getJavaFileObjects(file).iterator().next());
            }
        }
        return javaObjects;
    }
}
