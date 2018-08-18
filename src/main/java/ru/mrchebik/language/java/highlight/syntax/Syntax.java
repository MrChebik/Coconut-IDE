package ru.mrchebik.language.java.highlight.syntax;

import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import lombok.AllArgsConstructor;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.JavaCompilerSyntax;
import ru.mrchebik.language.java.highlight.syntax.switcher.symbolsolver.JavaSymbolSolverSyntax;
import ru.mrchebik.process.save.SaveTabsProcess;
import ru.mrchebik.settings.PropertyCollector;

import java.nio.file.Path;

@AllArgsConstructor
public class Syntax {
    private SaveTabsProcess saveTabsProcess;
    private TabPane tabPane;
    private TreeView<Path> treeView;

    public void compute(CustomCodeArea customCodeArea) {
        SyntaxWrapper syntax = PropertyCollector.isJDKCorrect() ?
                new JavaCompilerSyntax(customCodeArea, saveTabsProcess, tabPane, treeView)
                :
                new JavaSymbolSolverSyntax(customCodeArea);
        syntax.start();
    }
}
