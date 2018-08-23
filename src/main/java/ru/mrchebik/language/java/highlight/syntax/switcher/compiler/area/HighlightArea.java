package ru.mrchebik.language.java.highlight.syntax.switcher.compiler.area;

import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.JavaCompilerSyntax;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;

public class HighlightArea extends JavaCompilerSyntax {
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

        try {
            customCodeArea.codeAreaCSS.setStyleClass(start, end, "error");
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private static boolean isCurrArea(Diagnostic<? extends JavaFileObject> diagnostic) {
        String name = diagnostic.getSource().getName();
        int lastSlash = name.lastIndexOf(File.separator);
        String shortName = name.substring(lastSlash + 1);
        String areaName = customCodeArea.name;
        long startPosError = diagnostic.getStartPosition();

        return isCurrArea(shortName, areaName, startPosError);
    }

    private static boolean isCurrArea(String shortName, String areaName, long startPosError) {
        return shortName.equals(areaName) && startPosError > -1;
    }
}
