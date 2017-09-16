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
import ru.mrchebik.gui.places.creator.object.ObjectPlace;
import ru.mrchebik.gui.places.work.event.InputTextToOutputArea;
import ru.mrchebik.gui.places.work.event.structure.StructureUpdateGraphic;
import ru.mrchebik.gui.updater.tab.TabUpdater;
import ru.mrchebik.gui.updater.tree.CustomTreeItem;
import ru.mrchebik.gui.updater.tree.TreeUpdater;
import ru.mrchebik.model.CommandPath;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.Project;
import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.SaveTabs;
import ru.mrchebik.process.SaveTabsProcess;

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
        outputArea.setEditable(false);

        tabUpdater = new TabUpdater(tabPane);
        treeUpdater = new TreeUpdater(project, treeView, tabUpdater);
        treeUpdater.setRootToTreeView();
        executorCommand.setOutputArea(outputArea);

        inputTextToOutputArea = new InputTextToOutputArea(executorCommand);

        outputArea.setOnKeyPressed(inputTextToOutputArea);

        treeView.getSelectionModel().select(2);
        CustomIcons customIcons = new CustomIcons();
        treeView.getRoot().getChildren().get(1).setGraphic(new ImageView(customIcons.getFolderExpandImage()));

        TreeItem<Path> item = treeView.getRoot().getChildren().get(1).getChildren().get(0);

        treeView.getSelectionModel().select(item);

        String pathTarget = project.getPathSource() + File.separator + "Main.java";
        Path path = Paths.get(pathTarget);
        TreeItem<Path> root = treeView.getRoot();
        CustomTreeItem mainFile = (CustomTreeItem) treeUpdater.getItem(root, path);

        tabUpdater.addObjectToTab(mainFile);

        TextArea focusable = (TextArea) tabPane.getTabs().get(0).getContent();
        Platform.runLater(focusable::requestFocus);

        treeView.getTreeItem(3);

        commandPath = new CommandPath();
        treeView.setCellFactory(new StructureUpdateGraphic(project, objectPlace, commandPath));

        SaveTabsProcess saver = new SaveTabsProcess(tabPane);
        saver.start();
    }
}
