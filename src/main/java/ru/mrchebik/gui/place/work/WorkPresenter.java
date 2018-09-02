package ru.mrchebik.gui.place.work;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ru.mrchebik.build.Build;
import ru.mrchebik.build.BuildWrapper;
import ru.mrchebik.gui.collector.ComponentsCollector;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.gui.node.CustomTreeItem;
import ru.mrchebik.gui.node.treeCell.CustomTreeCell;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.gui.place.start.StartPresenter;
import ru.mrchebik.gui.place.work.event.InputTextToOutputArea;
import ru.mrchebik.gui.updater.TabUpdater;
import ru.mrchebik.gui.updater.TreeUpdater;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.language.Language;
import ru.mrchebik.language.java.highlight.Highlight;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.plugin.debug.cpu.PluginDebugCpu;
import ru.mrchebik.plugin.debug.ram.PluginDebugRam;
import ru.mrchebik.process.save.SaveTabs;
import ru.mrchebik.process.save.SaveTabsProcess;
import ru.mrchebik.project.Project;
import ru.mrchebik.util.GuiUtil;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class WorkPresenter extends KeyHelper implements Initializable {
    @FXML
    private TextArea outputArea;
    @FXML
    private TabPane tabPane;
    @FXML
    private TreeView<Path> treeView;
    @FXML
    private Button compile, run;
    @FXML
    private Label ram, cpu;
    @FXML
    private CheckMenuItem cpuItem, ramItem;

    private TabUpdater tabUpdater;

    private BuildWrapper build;

    @FXML
    private void handleNew() {
        StartPresenter.callStartup.callNewProject();
    }

    @FXML
    private void open() {
        StartPresenter.openProject();
    }

    @FXML
    private void callSettings() {
        // TODO
    }

    @FXML
    private void closeProject() {
        ViewHelper.WORK.stage.close();
        ViewHelper.START.start();
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private void pluginCpu() {
        cpu.setVisible(cpuItem.isSelected());

        if (!cpu.isVisible()) {
            cpu.setMinHeight(0);
            cpu.setMaxHeight(0);
        } else {
            cpu.setMinHeight(16);
            cpu.setMaxHeight(16);
        }
    }

    @FXML
    private void pluginRam() {
        ram.setVisible(ramItem.isSelected());

        if (!ram.isVisible()) {
            ram.setMinHeight(0);
            ram.setMaxHeight(0);
        } else {
            ram.setMinHeight(16);
            ram.setMaxHeight(16);
        }
    }

    @FXML
    private void about() {
        ViewHelper.ABOUT.start();
    }

    @FXML
    private void handleCompileProject() {
        handlePrepareToAction();

        build.compile();
    }

    @FXML
    private void handleDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            var selectionModel = treeView.getSelectionModel();
            var item = (CustomTreeItem) selectionModel.getSelectedItem();

            if (!item.isDirectory() && lengthOfOpenTabPathLessThanOne(item))
                tabUpdater.addObjectToTab(item);
        }
    }

    @FXML
    private void handleRunProject() {
        handlePrepareToAction();

        var path = (Path) tabPane.getSelectionModel().getSelectedItem().getUserData();
        build.run(path);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPlugins();
        initLocale(compile, run);
        initCollectorComponents();
        startSaveTabsProcess();
        initializeVariables();
        setUpOutputArea();
        setUpTreeView();
        addTabOfMain();
        moveCaretInMain();
    }

    private void initPlugins() {
        new PluginDebugCpu(cpu);
        new PluginDebugRam(ram);
    }

    private void initLocale(Button compile, Button run) {
        compile.setText(Locale.getProperty("compile_button", true));
        run.setText(Locale.getProperty("run_button", true));
    }

    private void initCollectorComponents() {
        ComponentsCollector.setComponents(outputArea, tabPane, treeView);
    }

    private void addTabOfMain() {
        var path = Paths.get(Project.pathSource.toString(), "Main.java");
        var root = treeView.getRoot();
        var mainFile = (CustomTreeItem) TreeUpdater.getItem(root, path);

        tabUpdater.addObjectToTab(mainFile);
    }

    private void moveCaretInMain() {
        var tabs = tabPane.getTabs();
        var firstTab = tabs.get(0);
        var codeArea = GuiUtil.getCodeAreaByTab(firstTab);

        if (Project.isOpen) codeArea.insertText(0, "");
        else {
            Platform.runLater(codeArea::requestFocus);
            codeArea.moveTo(73);
        }
    }

    private void handlePrepareToAction() {
        setEmptyValues();
        new SaveTabs(tabPane.getTabs()).start();
    }

    private void initializeVariables() {
        build = new Build(Language.command);

        Language.autocompleteAnalyser.start();

        Language.initAutocomplete(ViewHelper.WORK.stage);
        tabUpdater = new TabUpdater(tabPane, Highlight.create());

        TreeUpdater treeUpdater = new TreeUpdater(treeView, tabUpdater);
        treeUpdater.setRootToTreeView();
    }

    private boolean lengthOfOpenTabPathLessThanOne(CustomTreeItem item) {
        return tabPane.getTabs().filtered(tab -> item.getValue().equals(tab.getUserData())).size() < 1;
    }

    private void setEmptyValues() {
        outputArea.setText("");
        InputTextToOutputArea.input = "";
    }

    private void setUpOutputArea() {
        outputArea.setEditable(false);

        var event = new InputTextToOutputArea();
        outputArea.setOnKeyPressed(event);
    }

    private void setUpTreeView() {
        treeView.getSelectionModel().select(2);
        treeView.getRoot().getChildren().get(1).setGraphic(new ImageView(Icons.FOLDER_EXPAND.get()));

        var item = treeView.getRoot().getChildren().get(1).getChildren().get(0);

        treeView.getSelectionModel().select(item);
        treeView.setCellFactory(param -> new CustomTreeCell());
    }

    private void startSaveTabsProcess() {
        new SaveTabsProcess().start();
    }
}
