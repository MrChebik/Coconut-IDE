package ru.mrchebik.controller.javafx;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import ru.mrchebik.controller.Compile;
import ru.mrchebik.controller.Run;
import ru.mrchebik.controller.Save;
import ru.mrchebik.controller.exception.ExceptionUtils;
import ru.mrchebik.controller.process.SaveProcess;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.Project;
import ru.mrchebik.view.CreateF;
import ru.mrchebik.view.WorkStation;
import ru.mrchebik.view.treeview.FilePathTreeItem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by mrchebik on 8/29/17.
 */
public class WorkStationController implements Initializable {
    @FXML private TextArea code;
    @FXML private TextArea out;
    @FXML private Button compile;
    @FXML private Button run;
    @FXML
    private TreeView<Path> treeView;

    private TreeView<Path> backupTree;

    @FXML
    private BorderPane borderPane;
    @FXML
    private TabPane tabPane;

    private String command;
    private Path pathForCommand;

    private Path targetToRename;
    private Path renamedFile;

    private WorkStationController controller;

    @FXML private void handleRunProject() {
        Platform.runLater(() -> {
            out.setText("");

            saveAllOpenTabs();

            new Run((Path) tabPane.getSelectionModel().getSelectedItem().getUserData()).start();
        });
    }

    @FXML private void handleCompileProject() {
        Platform.runLater(() -> {
            out.setText("");

            saveAllOpenTabs();

            new Compile().start();
        });
    }

    private void saveAllOpenTabs() {
        List<Save> saves = controller.getTabs().stream().map(tab -> {
            TextArea area = (TextArea) tab.getContent();

            return new Save((Path) tab.getUserData(), area.getText());
        }).collect(Collectors.toList());

        saves.forEach(Thread::start);
        saves.forEach(ExceptionUtils.handlingConsumerWrapper(Thread::join, InterruptedException.class));
    }

    @FXML
    private void handleDoubleClick(MouseEvent e) throws IOException {
        if (e.getClickCount() == 2) {
            FilePathTreeItem item = (FilePathTreeItem) treeView.getSelectionModel().getSelectedItem();

            if (item != null && !item.isDirectory() && tabPane.getTabs().filtered(tab -> item.getValue().equals(tab.getUserData())).size() < 1) {
                Tab tabdata = new Tab();
                TextArea code = new TextArea();

                final String[] lines = {""};
                Files.readAllLines(Paths.get(item.getValue().toUri()))
                        .forEach(line -> lines[0] += line + "\n");
                code.setText(lines[0]);

                tabdata.setText(item.getValue().getFileName().toString());
                tabdata.setGraphic(new ImageView(CustomIcons.fileImage));
                tabdata.setUserData(item.getValue());
                tabdata.setContent(code);

                tabPane.getTabs().add(tabdata);
            }
        }
    }

    public String getCodeText() {
        return code.getText();
    }

    public void setOutText(String text) {
        out.setText(text);
    }

    public String getOutText() {
        return out.getText();
    }

    public ObservableList<Tab> getTabs() {
        return tabPane.getTabs();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = WorkStation.getFxmlLoader().getController();

        backupTree = new TreeView<>();

        loadTree();

        treeView.getSelectionModel().select(2);
        treeView.getTreeItem(2).setGraphic(new ImageView(CustomIcons.folderExpandImage));

        TreeItem<Path> item = treeView.getTreeItem(2).getChildren().get(0);

        treeView.getSelectionModel().select(item);

        Tab tabdata = new Tab();
        TextArea code = new TextArea();

        final String[] lines = {""};
        try {
            Files.readAllLines(Paths.get(item.getValue().toUri()))
                    .forEach(line -> lines[0] += line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        code.setText(lines[0]);

        tabdata.setText(item.getValue().getFileName().toString());
        tabdata.setGraphic(new ImageView(CustomIcons.fileImage));
        tabdata.setUserData(item.getValue());
        tabdata.setContent(code);

        tabPane.getTabs().add(tabdata);

        treeView.getTreeItem(3);

        treeView.setCellFactory(treeView -> new TreeCell<Path>() {
            @Override
            public void updateItem(Path path, boolean empty) {
                super.updateItem(path, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                    setContextMenu(null);
                } else {
                    boolean isDirectory = path.toFile().isDirectory();

                    setText(path.getFileName().toString());

                    setGraphic(getTreeItem().getGraphic());

                    ContextMenu contextMenu = new ContextMenu();

                    MenuItem createFile = new MenuItem("Create File");
                    MenuItem createFolder = new MenuItem("Create Folder");
                    MenuItem cut = new MenuItem("Cut");
                    MenuItem copy = new MenuItem("Copy");
                    MenuItem paste = new MenuItem("Paste");
                    MenuItem rename = new MenuItem("Rename");
                    MenuItem delete = new MenuItem("Delete");

                    if (isDirectory) {
                        createFile.setOnAction(event -> CreateF.start("Create File", path));

                        createFolder.setOnAction(event -> CreateF.start("Create Folder", path));
                    } else {
                        createFile.setDisable(true);
                        createFolder.setDisable(true);
                        paste.setDisable(true);
                    }

                    cut.setOnAction(event -> {
                        command = "Cut";
                        pathForCommand = path;
                    });

                    copy.setOnAction(event -> {
                        command = "Copy";
                        pathForCommand = path;
                    });

                    paste.setOnAction(event -> {
                        Path moveTo = Paths.get(path.toString() + File.separator + pathForCommand.getFileName().toString());
                        if ("Cut".equals(command)) {
                            try {
                                Files.move(pathForCommand, moveTo, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if ("Copy".equals(command)) {
                            try {
                                Files.copy(pathForCommand, moveTo, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        loadTree();
                    });

                    rename.setOnAction(event -> CreateF.start("Rename " + (isDirectory ? "Folder" : "File"), path));

                    delete.setOnAction(event -> {
                        deleteDirectory(path.toFile());
                        loadTree();
                    });

                    contextMenu.getItems().addAll(createFile, createFolder, new SeparatorMenuItem(), cut, copy, paste, new SeparatorMenuItem(), rename, new SeparatorMenuItem(), delete);
                    setContextMenu(contextMenu);
                }
            }
        });

        new SaveProcess().start();
    }

    public void setTargetToRename(Path targetToRename) {
        this.targetToRename = targetToRename;
    }

    public void setRenamedFile(Path renamedFile) {
        this.renamedFile = renamedFile;
    }

    public void loadTree() {
        if (treeView.getRoot() != null) {
            backupTree.setRoot(deepcopy(treeView.getRoot()));
        }

        int index = treeView.getRow(treeView.getSelectionModel().getSelectedItem());

        TreeItem<Path> rootNode = new FilePathTreeItem(Paths.get(Project.getPath()));
        rootNode.setExpanded(true);
        rootNode.setGraphic(new ImageView(CustomIcons.folderExpandImage));
        rootNode.expandedProperty().addListener((observable, oldValue, newValue) -> {
            BooleanProperty bb = (BooleanProperty) observable;

            TreeItem t = (TreeItem) bb.getBean();

            t.setGraphic(new ImageView(newValue ? CustomIcons.folderExpandImage : CustomIcons.folderCollapseImage));
        });

        treeView.setRoot(rootNode);

        if (backupTree.getRoot() != null) {
            passAllTree(backupTree.getRoot());
        }

        treeView.getSelectionModel().select(index);
    }

    private TreeItem<Path> deepcopy(TreeItem<Path> item) {
        TreeItem<Path> copy = new TreeItem<>(item.getValue());
        copy.setExpanded(item.isExpanded());
        for (TreeItem<Path> child : item.getChildren()) {
            copy.getChildren().add(deepcopy(child));
        }
        return copy;
    }

    private void passAllTree(TreeItem<Path> root) {
        TreeItem item = getItem(treeView.getRoot(), root.getValue());

        if (item != null) {
            item.setExpanded(root.isExpanded());
            item.setGraphic(new ImageView(item.isExpanded() ? CustomIcons.folderExpandImage : CustomIcons.folderCollapseImage));
        }

        for (TreeItem<Path> child : root.getChildren()) {
            if (Files.isDirectory(child.getValue())) {
                passAllTree(child);
            }
        }
    }

    private TreeItem<Path> getItem(TreeItem<Path> root, Path path) {
        System.out.println(" --- ");
        System.out.println(root.getValue() + " // " + path);
        if (root.getValue().equals(path)) {
            return root;
        }

        if (path.equals(targetToRename) && root.getValue().equals(renamedFile)) {
            targetToRename = null;
            renamedFile = null;

            return root;
        }

        for (TreeItem<Path> child : root.getChildren()) {
            System.out.println(child.getValue() + " // " + path);
            if (!path.equals(child.getValue())) {
                if (path.equals(targetToRename) && child.getValue().equals(renamedFile)) {
                    targetToRename = null;
                    renamedFile = null;

                    return child;
                }
                if (!child.getChildren().isEmpty()) {
                    TreeItem<Path> item = getItem(child, path);
                    if (item != null) {
                        return item;
                    }
                }
            } else {
                return child;
            }
        }

        return null;
    }

    private void deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }

        path.delete();
    }
}
