package ru.mrchebik.highlight.syntax.switcher.javaCompiler.area;

import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.highlight.syntax.switcher.javaCompiler.JavaCompilerSyntax;
import ru.mrchebik.model.Project;
import ru.mrchebik.process.SaveTabsProcess;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.nio.file.Path;

public class HighlightArea extends JavaCompilerSyntax {
    public HighlightArea(CustomCodeArea customCodeArea, Project project, SaveTabsProcess saveTabsProcess, TabPane tabPane, TreeView<Path> treeView) {
        super(customCodeArea, project, saveTabsProcess, tabPane, treeView);
    }

    public static void highlightArea() {
        diagnostics.stream()
                .filter(JavaCompilerSyntax::isErrorKind)
                .filter(HighlightArea::isStartPosMoreThanMinusOne)
                .filter(HighlightArea::isCurrArea)
                .forEach(HighlightArea::highlightErrorInArea);
    }

    private static boolean isStartPosMoreThanMinusOne(Diagnostic diagnostic) {
        return diagnostic.getStartPosition() > -1;
    }

    private static void highlightErrorInArea(Diagnostic diagnostic) {
        int start = (int) diagnostic.getStartPosition();
        int end = (int) diagnostic.getEndPosition();
        int areaSize = customCodeArea.getText().length() - 1;

        if (start == end) {
            if (start > 0) {
                start--;
            }
            if (end < areaSize) {
                end++;
            }
        } else if (end < start) {
            end = start + 1;
            start--;
            if (end == areaSize) {
                end--;
            }
        }

        customCodeArea.getCodeAreaCSS().setStyleClass(start, end, "error");
    }

    private static boolean isCurrArea(Diagnostic<? extends JavaFileObject> diagnostic) {
        String name = diagnostic.getSource().getName();
        int lastSlash = name.lastIndexOf(File.separator);
        String shortName = name.substring(lastSlash + 1);
        String areaName = customCodeArea.getName();
        long startPosError = diagnostic.getStartPosition();

        return isCurrArea(shortName, areaName, startPosError);
    }

    private static boolean isCurrArea(String shortName, String areaName, long startPosError) {
        return shortName.equals(areaName) && startPosError > -1;
    }
}
