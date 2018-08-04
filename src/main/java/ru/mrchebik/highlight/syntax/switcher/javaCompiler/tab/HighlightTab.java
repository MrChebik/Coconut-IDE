package ru.mrchebik.highlight.syntax.switcher.javaCompiler.tab;

import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.highlight.syntax.switcher.javaCompiler.JavaCompilerSyntax;
import ru.mrchebik.model.Project;
import ru.mrchebik.process.SaveTabsProcess;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.nio.file.Path;

public class HighlightTab extends JavaCompilerSyntax {
    private static final String TRANSPARENT = "close-color: white;";
    private static final String ERROR = "-fx-background-color: #cc1b00; -fx-text-base-color: white; close-color: black; -fx-text-fill: white";

    public HighlightTab(CustomCodeArea customCodeArea, Project project, SaveTabsProcess saveTabsProcess, TabPane tabPane, TreeView<Path> treeView) {
        super(customCodeArea, project, saveTabsProcess, tabPane, treeView);
    }

    public static void highlight() {
        if (diagnostics != null) {
            tabPane.getTabs().forEach(t -> {
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
