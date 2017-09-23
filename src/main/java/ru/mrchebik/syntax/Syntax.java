package ru.mrchebik.syntax;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Problem;
import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import lombok.AllArgsConstructor;
import ru.mrchebik.gui.node.CustomCodeArea;
import ru.mrchebik.model.Project;
import ru.mrchebik.process.SaveTabsProcess;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class Syntax {
    private Project project;
    private SaveTabsProcess saveTabsProcess;
    private TabPane tabPane;
    private TreeView<Path> treeView;

    public void compute(CustomCodeArea customCodeArea) {
        if (/*Files.exists(Paths.get(System.getProperty("java.home"), "bin", "javac"))*/ true) {
            ErrorProcessSyntax processSyntax = new ErrorProcessSyntax(customCodeArea, project, saveTabsProcess, tabPane, treeView);
            processSyntax.start();
        } else {
            //TypeSolver typeSolver = new CombinedTypeSolver();
            String code = customCodeArea.getText();

            //CompilationUnit cu;
            try {
                /*cu = */JavaParser.parse(customCodeArea.getText());
            } catch (ParseProblemException ppe) {
                for (Problem problem : ppe.getProblems()) {
                    //System.out.println(ppe.getProblems().get(0).getMessage());
                    //System.out.println(ppe.getProblems().get(0).getLocation());
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(problem.getVerboseMessage());
                    Matcher foundMatcher = Pattern.compile("\"[^\"]+").matcher(problem.getMessage());
                    String found = "";
                    if (foundMatcher.find()) {
                        found = foundMatcher.group().substring(1);
                    }
                    if (!found.isEmpty() && problem.getLocation().isPresent()) {
                        int startFound = problem.getLocation().get().toString().indexOf(found);
                        int line = 0;
                        if (matcher.find()) {
                            line = Integer.parseInt(matcher.group());
                        }
                        int column = 0;
                        if (matcher.find()) {
                            column = Integer.parseInt(matcher.group());
                        }

                        int beforeChars = 0;
                        int n = 1;
                        for (int i = 0; i < code.length(); i++) {
                            if (code.charAt(i) == '\n') {
                                n++;
                            }
                            if (n == line) {
                                break;
                            } else {
                                beforeChars++;
                            }
                        }

                        customCodeArea.getCodeAreaCSS().setStyleClass(beforeChars + column + startFound, (beforeChars + column + startFound) + found.length(), "error");
                    }
                }

                Platform.runLater(() -> customCodeArea.setStyleSpans(0, customCodeArea.getCodeAreaCSS().getStyleSpans(0, customCodeArea.getText().length())));
            }

            /*List<VariableDeclarationExpr> variableDeclarationExprs = Navigator.findAllNodesOfGivenClass(cu, VariableDeclarationExpr.class);

            variableDeclarationExprs.forEach(a -> {
                int beforeChars = 0;
                int n = 1;
                for (int i = 0; i < code.length(); i++) {
                    if (code.charAt(i) == '\n') {
                        n++;
                    }
                    if (n == a.getBegin().get().line) {
                        break;
                    } else {
                        beforeChars++;
                    }
                }

                customCodeArea.setStyleClass(beforeChars + a.getBegin().get().column, beforeChars + a.getBegin().get().column + a.toString().length(), "variable");
            });*/
        }
    }
}
