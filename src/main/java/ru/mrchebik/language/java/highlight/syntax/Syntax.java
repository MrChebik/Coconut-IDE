package ru.mrchebik.language.java.highlight.syntax;

import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.JavaCompilerSyntax;
import ru.mrchebik.language.java.highlight.syntax.switcher.symbolsolver.JavaSymbolSolverSyntax;
import ru.mrchebik.process.save.SaveTabsProcess;
import ru.mrchebik.project.Project;
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

    public void compute(CustomCodeArea customCodeArea) {
        if (PropertyCollector.isJDKCorrect()) {
            JavaCompilerSyntax processSyntax = new JavaCompilerSyntax(customCodeArea, project, saveTabsProcess, tabPane, treeView);
            processSyntax.start();
        } else {
            JavaSymbolSolverSyntax processSyntax = new JavaSymbolSolverSyntax(customCodeArea);
            processSyntax.start();
        }
    }
}
