package ru.mrchebik.gui.place.work;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.fxmisc.flowless.ScaledVirtualized;
import org.fxmisc.flowless.VirtualizedScrollPane;
import ru.mrchebik.autocomplete.AnalyzerAutocomplete;
import ru.mrchebik.build.Build;
import ru.mrchebik.build.BuildWrapper;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.gui.node.CustomTreeItem;
import ru.mrchebik.gui.node.codearea.CustomCodeArea;
import ru.mrchebik.gui.place.menu.create.file.CreateFilePlace;
import ru.mrchebik.gui.place.menu.create.folder.CreateFolderPlace;
import ru.mrchebik.gui.place.menu.rename.file.RenameFilePlace;
import ru.mrchebik.gui.place.menu.rename.folder.RenameFolderPlace;
import ru.mrchebik.gui.place.work.event.InputTextToOutputArea;
import ru.mrchebik.gui.place.work.event.structure.StructureUpdateGraphic;
import ru.mrchebik.gui.updater.TabUpdater;
import ru.mrchebik.gui.updater.TreeUpdater;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.injection.ComponentsCollector;
import ru.mrchebik.language.Language;
import ru.mrchebik.language.java.highlight.Highlight;
import ru.mrchebik.language.java.symbols.SymbolsType;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.process.save.SaveTabs;
import ru.mrchebik.process.save.SaveTabsProcess;
import ru.mrchebik.project.Project;

import javax.inject.Inject;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
    @Inject
    private WorkPlace workPlace;

    private TabUpdater tabUpdater;

    private BuildWrapper build;

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
        initLocale(compile, run);
        initCollectorComponents();
        startSaveTabsProcess();
        initializeVariables();
        setUpOutputArea();
        setUpTreeView();
        addTabOfMain();
        moveCaretInMain();
    }

    private void initLocale(Button compile, Button run) {
        compile.setText(Locale.COMPILE_BUTTON);
        run.setText(Locale.RUN_BUTTON);
    }

    private void initCollectorComponents() {
        var createFilePlace = new CreateFilePlace();
        var createFolderPlace = new CreateFolderPlace();
        var renameFilePlace = new RenameFilePlace();
        var renameFolderPlace = new RenameFolderPlace();

        ComponentsCollector.setComponents(outputArea, tabPane, treeView, createFilePlace, createFolderPlace, renameFilePlace, renameFolderPlace);
    }

    private void addTabOfMain() {
        var path = Paths.get(Project.pathSource.toString(), "Main.java");
        var root = treeView.getRoot();
        var mainFile = (CustomTreeItem) TreeUpdater.getItem(root, path);

        tabUpdater.addObjectToTab(mainFile);
    }

    private void moveCaretInMain() {
        var scrollPane = (VirtualizedScrollPane) tabPane.getTabs().get(0).getContent();
        var scaledVirtualized = (ScaledVirtualized) scrollPane.getContent();
        var area = (CustomCodeArea) scaledVirtualized.getChildrenUnmodifiable().get(0);
        Platform.runLater(area::requestFocus);
        area.moveTo(73);
    }

    private void handlePrepareToAction() {
        setEmptyValues();
        SaveTabs.create(tabPane.getTabs()).start();
    }

    private void initializeVariables() {
        build = new Build(Language.command);

        AnalyzerAutocomplete.initialize(Project.pathSource);
        AnalyzerAutocomplete.database.setKeywords(Arrays.asList(SymbolsType.KEYWORDS.getSymbols()));

        tabUpdater = new TabUpdater(tabPane, Highlight.create(), workPlace.getStage());

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
        treeView.setCellFactory(new StructureUpdateGraphic());
    }

    private void startSaveTabsProcess() {
        new SaveTabsProcess().start();
    }
}
