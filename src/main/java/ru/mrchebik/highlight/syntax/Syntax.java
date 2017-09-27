package ru.mrchebik.highlight.syntax;

import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.mrchebik.gui.node.CustomCodeArea;
import ru.mrchebik.highlight.syntax.switcher.javaCompiler.JavaCompilerSyntax;
import ru.mrchebik.highlight.syntax.switcher.javaSymbolSolver.JavaSymbolSolverSyntax;
import ru.mrchebik.model.Project;
import ru.mrchebik.process.SaveTabsProcess;
import ru.mrchebik.settings.PropertyCollector;

import java.nio.file.Path;

@RequiredArgsConstructor
public class Syntax {
    @NonNull
    private Project project;
    @NonNull
    private SaveTabsProcess saveTabsProcess;
    @NonNull
    private TabPane tabPane;
    @NonNull
    private TreeView<Path> treeView;

    private PropertyCollector propertyCollector;
    private boolean isJDKCorrect;

    public void compute(CustomCodeArea customCodeArea) {
        if (propertyCollector == null) {
            propertyCollector = PropertyCollector.create();
            isJDKCorrect = propertyCollector.isJDKCorrect();
        }

        if (isJDKCorrect) {
            JavaCompilerSyntax processSyntax = new JavaCompilerSyntax(customCodeArea, project, saveTabsProcess, tabPane, treeView);
            processSyntax.start();
        } else {
            JavaSymbolSolverSyntax processSyntax = new JavaSymbolSolverSyntax();
            processSyntax.start();
        }
    }
}
