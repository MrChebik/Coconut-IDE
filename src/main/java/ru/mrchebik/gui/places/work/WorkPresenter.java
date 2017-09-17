package ru.mrchebik.gui.places.work;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.gui.places.creator.object.ObjectPlace;
import ru.mrchebik.gui.places.work.event.InputTextToOutputArea;
import ru.mrchebik.gui.places.work.event.structure.StructureUpdateGraphic;
import ru.mrchebik.gui.updater.tab.TabUpdater;
import ru.mrchebik.gui.updater.tree.CustomTreeItem;
import ru.mrchebik.gui.updater.tree.TreeUpdater;
import ru.mrchebik.model.CommandPath;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.Project;
import ru.mrchebik.model.syntax.Highlight;
import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.SaveTabs;
import ru.mrchebik.process.SaveTabsProcess;
import ru.mrchebik.process.io.ErrorProcess;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/29/17.
 */
public class WorkPresenter implements Initializable {
    @FXML private TextArea outputArea;
    @FXML private TabPane tabPane;
    @FXML private TreeView<Path> treeView;

    @Inject private Project project;
    @Inject private ObjectPlace objectPlace;
    @Inject private ExecutorCommand executorCommand;
    @Inject private ErrorProcess errorProcess;

    private TabUpdater tabUpdater;
    private TreeUpdater treeUpdater;
    private InputTextToOutputArea inputTextToOutputArea;
    private CommandPath commandPath;

    @FXML private void handleRunProject() {
        Platform.runLater(() -> {
            handlePrepareToAction();

            Path path = (Path) tabPane.getSelectionModel().getSelectedItem().getUserData();
            project.run(path, outputArea);
        });
    }

    @FXML private void handleCompileProject() {
        Platform.runLater(() -> {
            handlePrepareToAction();

            project.compile();
        });
    }

    private void handlePrepareToAction() {
        setEmptyValues();
        saveAllOpenTabs();
    }

    private void setEmptyValues() {
        outputArea.setText("");
        inputTextToOutputArea.setInput("");
    }

    private void saveAllOpenTabs() {
        SaveTabs saver = new SaveTabs(tabPane.getTabs());
        saver.start();
    }

    @FXML
    private void handleDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            CustomTreeItem item = (CustomTreeItem) treeView.getSelectionModel().getSelectedItem();

            if (/*item != null && */!item.isDirectory() && lengthOfOpenTabPathLessThanOne(item))
                tabUpdater.addObjectToTab(item);
        }
    }

    private boolean lengthOfOpenTabPathLessThanOne(CustomTreeItem item) {
        return tabPane.getTabs().filtered(tab -> item.getValue().equals(tab.getUserData())).size() < 1;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initalizeVariables();
        setUpOutputArea();
        setUpTreeView();
        addTabOfMain();
        focusToMain();
        activateSaveProcess();
    }

    private void initalizeVariables() {
        Highlight highlight = new Highlight();

        errorProcess.setTextArea(outputArea);

        commandPath = new CommandPath();
        tabUpdater = new TabUpdater(tabPane, highlight);
        treeUpdater = new TreeUpdater(project, treeView, tabUpdater);
        treeUpdater.setRootToTreeView();
        executorCommand.setOutputArea(outputArea);
        executorCommand.setErrorProcess(errorProcess);

        inputTextToOutputArea = new InputTextToOutputArea(executorCommand, outputArea);
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
        treeView.setCellFactory(new StructureUpdateGraphic(project, objectPlace, commandPath));
    }

    private void addTabOfMain() {
        String pathTarget = project.getPathSource() + File.separator + "Main.java";
        Path path = Paths.get(pathTarget);
        TreeItem<Path> root = treeView.getRoot();
        CustomTreeItem mainFile = (CustomTreeItem) treeUpdater.getItem(root, path);

        tabUpdater.addObjectToTab(mainFile);
    }

    private void focusToMain() {
        VirtualizedScrollPane scrollPane = (VirtualizedScrollPane) tabPane.getTabs().get(0).getContent();
        CodeArea focusable = (CodeArea) scrollPane.getContent();
        Platform.runLater(focusable::requestFocus);
        focusable.moveTo(73);
    }

    private void activateSaveProcess() {
        SaveTabsProcess saver = new SaveTabsProcess(tabPane);
        saver.start();
    }
}
