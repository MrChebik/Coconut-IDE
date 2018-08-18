package ru.mrchebik.language.java.highlight.syntax.switcher.compiler.tab;

import ru.mrchebik.injection.CollectorComponents;
import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.JavaCompilerSyntax;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.nio.file.Path;

public class HighlightTab extends JavaCompilerSyntax {
    private static final String TRANSPARENT = "close-color: white;";
    private static final String ERROR = "-fx-background-color: #cc1b00; -fx-text-base-color: white; close-color: black; -fx-text-fill: white";

    public static void highlight() {
        if (diagnostics != null) {
            CollectorComponents.tabPane.getTabs().forEach(t -> {
                Path path = (Path) t.getUserData();
                for (Diagnostic diagnostic : diagnostics) {
                    if (!"WARNING".equals(diagnostic.getKind().toString())) {
                        JavaFileObject javaFileObject = (JavaFileObject) diagnostic.getSource();
                        if (javaFileObject.getName().equals(path.toString())) {
                            t.setStyle(ERROR);
                            return;
                        }
                    }
                }

                t.setStyle(TRANSPARENT);
            });
        }
    }
}
