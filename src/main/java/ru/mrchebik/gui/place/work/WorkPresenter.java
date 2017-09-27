package ru.mrchebik.gui.place.work;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.gui.node.CustomTreeItem;
import ru.mrchebik.gui.place.work.event.InputTextToOutputArea;
import ru.mrchebik.gui.place.work.event.structure.StructureUpdateGraphic;
import ru.mrchebik.gui.updater.TabUpdater;
import ru.mrchebik.gui.updater.TreeUpdater;
import ru.mrchebik.highlight.Highlight;
import ru.mrchebik.highlight.syntax.Syntax;
import ru.mrchebik.model.ActionPlaces;
import ru.mrchebik.model.CommandPath;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.Project;
import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.SaveTabs;
import ru.mrchebik.process.SaveTabsProcess;
import ru.mrchebik.process.io.ErrorProcess;

import javax.inject.Inject;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class WorkPresenter implements Initializable {
    @FXML
    private TextArea outputArea;
    @FXML
    private TabPane tabPane;
    @FXML
    private TreeView<Path> treeView;
    @Inject
    private ErrorProcess errorProcess;
    @Inject
    private ExecutorCommand executorCommand;
    @Inject
    private ActionPlaces places;
    @Inject
    private Project project;

    private CommandPath commandPath;
    private InputTextToOutputArea inputTextToOutputArea;
    private SaveTabsProcess saveTabsProcess;
    private TabUpdater tabUpdater;
    private TreeUpdater treeUpdater;

    @FXML
    private void handleCompileProject() {
        Platform.runLater(() -> {
            handlePrepareToAction();

            project.compile();
        });
    }

    @FXML
    private void handleDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            SelectionModel selectionModel = treeView.getSelectionModel();
            CustomTreeItem item = (CustomTreeItem) selectionModel.getSelectedItem();

            if (!item.isDirectory() && lengthOfOpenTabPathLessThanOne(item)) {
                tabUpdater.addObjectToTab(item);
            }
        }
    }

    @FXML
    private void handleRunProject() {
        Platform.runLater(() -> {
            handlePrepareToAction();

            Path path = (Path) tabPane.getSelectionModel().getSelectedItem().getUserData();
            project.run(path);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startSaveTabsProcess();
        initializeVariables();
        setUpOutputArea();
        setUpTreeView();
        addTabOfMain();
        moveCaretInMain();
    }

    private void addTabOfMain() {
        Path path = Paths.get(project.getPathSource().toString(), "Main.java");
        TreeItem<Path> root = treeView.getRoot();
        CustomTreeItem mainFile = (CustomTreeItem) treeUpdater.getItem(root, path);

        tabUpdater.addObjectToTab(mainFile);
    }

    private void moveCaretInMain() {
        VirtualizedScrollPane scrollPane = (VirtualizedScrollPane) tabPane.getTabs().get(0).getContent();
        CodeArea area = (CodeArea) scrollPane.getContent();
        Platform.runLater(area::requestFocus);
        area.moveTo(73);
    }

    private void handlePrepareToAction() {
        setEmptyValues();
        SaveTabs.create(tabPane.getTabs()).start();
    }

    private void initializeVariables() {
        errorProcess.setTextArea(outputArea);

        commandPath = CommandPath.create();

        Syntax syntax = new Syntax(project, saveTabsProcess, tabPane, treeView);
        tabUpdater = new TabUpdater(tabPane, Highlight.create(), syntax);

        treeUpdater = new TreeUpdater(project, treeView, tabUpdater);
        treeUpdater.setRootToTreeView();

        executorCommand.setOutputArea(outputArea);
        executorCommand.setErrorProcess(errorProcess);

        inputTextToOutputArea = new InputTextToOutputArea(executorCommand);
    }

    private boolean lengthOfOpenTabPathLessThanOne(CustomTreeItem item) {
        return tabPane.getTabs().filtered(tab -> item.getValue().equals(tab.getUserData())).size() < 1;
    }

    private void setEmptyValues() {
        outputArea.setText("");
        inputTextToOutputArea.setInput("");
    }

    private void setUpOutputArea() {
        outputArea.setEditable(false);
        outputArea.setOnKeyPressed(inputTextToOutputArea);
    }

    private void setUpTreeView() {
        treeView.getSelectionModel().select(2);
        CustomIcons customIcons = new CustomIcons();
        treeView.getRoot().getChildren().get(1).setGraphic(new ImageView(customIcons.getFolderExpandImage()));

        TreeItem<Path> item = treeView.getRoot().getChildren().get(1).getChildren().get(0);

        treeView.getSelectionModel().select(item);
        treeView.setCellFactory(new StructureUpdateGraphic(commandPath, places, project));
    }

    private void startSaveTabsProcess() {
        saveTabsProcess = SaveTabsProcess.create(tabPane);
        saveTabsProcess.start();
    }
}
