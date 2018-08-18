package ru.mrchebik.language.java.highlight.syntax.switcher.compiler;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.language.Language;
import ru.mrchebik.language.java.highlight.syntax.SyntaxWrapper;
import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.area.HighlightArea;
import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.cell.HighlightCell;
import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.tab.HighlightTab;
import ru.mrchebik.process.save.SaveTabsProcess;
import ru.mrchebik.project.Project;

import javax.tools.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class JavaCompilerSyntax extends Thread implements SyntaxWrapper {
    protected static CustomCodeArea customCodeArea;
    private SaveTabsProcess saveTabsProcess;
    protected static TabPane tabPane;
    protected static TreeView<Path> treeView;

    public static List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public JavaCompilerSyntax(CustomCodeArea customCodeArea,
                       SaveTabsProcess saveTabsProcess,
                       TabPane tabPane,
                       TreeView<Path> treeView) {
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

        String[] compileOptions = new String[]{"-d", Project.pathOut.toString()};
        Iterable<String> compilationOptions = Arrays.asList(compileOptions);

        List<JavaFileObject> javaObjects = scanRecursivelyForJavaObjects(Project.pathSource.toFile(), fileManager);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, compilationOptions, null, javaObjects);

        task.call();
        JavaCompilerSyntax.diagnostics = diagnostics.getDiagnostics();
        HighlightCell.highlight();
        HighlightTab.highlight();
        HighlightArea.highlightArea();

        Platform.runLater(() -> {
            try {
                customCodeArea.setStyleSpans(0, customCodeArea.getCodeAreaCSS().getStyleSpans(0, customCodeArea.getText().length()));
                Language.caretHighlight.compute(customCodeArea);
            } catch (IndexOutOfBoundsException ignored) {}
        });
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
