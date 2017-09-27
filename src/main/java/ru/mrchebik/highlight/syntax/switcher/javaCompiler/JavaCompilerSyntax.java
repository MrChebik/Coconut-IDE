package ru.mrchebik.highlight.syntax.switcher.javaCompiler;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import ru.mrchebik.gui.node.CustomCodeArea;
import ru.mrchebik.highlight.syntax.switcher.javaCompiler.cell.HighlightCell;
import ru.mrchebik.highlight.syntax.switcher.javaCompiler.tab.HighlightTab;
import ru.mrchebik.model.Project;
import ru.mrchebik.process.SaveTabsProcess;

import javax.tools.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static ru.mrchebik.highlight.syntax.switcher.javaCompiler.area.HighlightArea.highlightArea;

public class JavaCompilerSyntax extends Thread {
    protected static CustomCodeArea customCodeArea;
    private Project project;
    private SaveTabsProcess saveTabsProcess;
    protected static TabPane tabPane;
    protected static TreeView<Path> treeView;

    public static List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public JavaCompilerSyntax(CustomCodeArea customCodeArea,
                       Project project,
                       SaveTabsProcess saveTabsProcess,
                       TabPane tabPane,
                       TreeView<Path> treeView) {
        this.project = project;
        this.saveTabsProcess = saveTabsProcess;
        JavaCompilerSyntax.customCodeArea = customCodeArea;
        JavaCompilerSyntax.tabPane = tabPane;
        JavaCompilerSyntax.treeView = treeView;
    }

    @Override
    public void run() {
        saveTabsProcess.runSynch();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), null);

        String[] compileOptions = new String[]{"-d", project.getPathOut().toString()};
        Iterable<String> compilationOptions = Arrays.asList(compileOptions);

        List<JavaFileObject> javaObjects = scanRecursivelyForJavaObjects(project.getPathSource().toFile(), fileManager);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, compilationOptions, null, javaObjects);

        task.call();
        JavaCompilerSyntax.diagnostics = diagnostics.getDiagnostics();
        HighlightCell.highlight();
        HighlightTab.highlight();
        highlightArea();

        Platform.runLater(() -> customCodeArea.setStyleSpans(0, customCodeArea.getCodeAreaCSS().getStyleSpans(0, customCodeArea.getText().length())));
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

    private static boolean isError(String kind) {
        return "ERROR".equals(kind);
    }

    protected static boolean isErrorKind(Diagnostic diagnostic) {
        String kind = diagnostic.getKind().toString();

        return isError(kind);
    }
}
