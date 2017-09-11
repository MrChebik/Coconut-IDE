package ru.mrchebik.controller.javafx;

import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import ru.mrchebik.controller.actions.Compile;
import ru.mrchebik.controller.actions.Run;
import ru.mrchebik.controller.actions.autosave.Autosave;
import ru.mrchebik.controller.actions.autosave.saver.SaveTabs;
import ru.mrchebik.controller.actions.autosave.saver.SaveTabsProcess;
import ru.mrchebik.controller.javafx.updater.tab.TabUpdater;
import ru.mrchebik.controller.javafx.updater.tree.CustomTreeItem;
import ru.mrchebik.controller.javafx.updater.tree.TreeUpdater;
import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.project.Project;
import ru.mrchebik.view.CreatorFiles;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/29/17.
 */
public class WorkStationController implements Initializable {
    @Inject
    private Project project;

    @FXML private TextArea out;
    private String input;

    @FXML
    private TreeView<Path> treeView;

    @FXML
    private TabPane tabPane;

    private String command;
    private Path pathForCommand;

    @FXML private void handleRunProject() {
        Platform.runLater(() -> {
            out.setText("");
            input = "";

            saveAllOpenTabs();

            new Run((Path) tabPane.getSelectionModel().getSelectedItem().getUserData(), this).start();
        });
    }

    @FXML private void handleCompileProject() {
        Platform.runLater(() -> {
            out.setText("");
            input = "";

            saveAllOpenTabs();

            new Compile().start();
        });
    }

    private void saveAllOpenTabs() {
        Autosave saver = new SaveTabs(this.getTabs());
        saver.start();
        saver.save();
    }

    @FXML
    private void handleDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            CustomTreeItem item = (CustomTreeItem) treeView.getSelectionModel().getSelectedItem();

            if (item != null && !item.isDirectory() && tabPane.getTabs().filtered(tab -> item.getValue().equals(tab.getUserData())).size() < 1) {
                TabUpdater.addObjectToTab(item);
            }
        }
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
        TreeUpdater.setTreeView(treeView);
        TreeUpdater.setRootToTreeView();

        TabUpdater.setTabPane(tabPane);

        input = "";

        out.setOnKeyPressed(event -> {
            if (EnhancedProcess.getOutputStream() != null) {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        EnhancedProcess.getOutputStream().write((input.replaceAll("\r", "") + "\n").replaceAll("\b", "").getBytes());
                        EnhancedProcess.getOutputStream().flush();
                    } catch (IOException ignored) {
                        EnhancedProcess.setOutputStream();
                    } finally {
                        input = "";
                    }
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (input.length() > 0) {
                        input = input.substring(0, input.length() - 1);
                    }
                } else {
                    input = input.concat(event.getText());
                }
            }
        });

        treeView.getSelectionModel().select(2);
        treeView.getTreeItem(2).setGraphic(new ImageView(CustomIcons.getFolderExpandImage()));

        TreeItem<Path> item = treeView.getTreeItem(2).getChildren().get(0);

        treeView.getSelectionModel().select(item);

        String pathTarget = project.getPathSource() + File.separator + "Main.java";
        Path path = Paths.get(pathTarget);
        TreeItem<Path> root = treeView.getRoot();
        CustomTreeItem mainFile = (CustomTreeItem) TreeUpdater.getItem(root, path);

        TabUpdater.addObjectToTab(mainFile);

        TextArea focusable = (TextArea) tabPane.getTabs().get(0).getContent();
        Platform.runLater(focusable::requestFocus);

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
                    String pathString = path.toString();
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

                    if (!isDirectory) {
                        createFile.setDisable(true);
                        createFolder.setDisable(true);
                        paste.setDisable(true);
                    }
                    if (path.equals(project.getPath())) {
                        cut.setDisable(true);
                        copy.setDisable(true);
                        paste.setDisable(true);
                        delete.setDisable(true);
                    }

                    createFile.setOnAction(event -> CreatorFiles.start("Create File", path));

                    createFolder.setOnAction(event -> CreatorFiles.start("Create Folder", path));

                    cut.setOnAction(event -> {
                        command = "Cut";
                        pathForCommand = path;
                    });

                    copy.setOnAction(event -> {
                        command = "Copy";
                        pathForCommand = path;
                    });

                    paste.setOnAction(event -> {
                        if (command != null) {
                            Path moveTo = Paths.get(pathString + File.separator + pathForCommand.getFileName().toString());
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
                        }
                    });

                    rename.setOnAction(event -> CreatorFiles.start("Rename " + (isDirectory ? "Folder" : "File"), path));

                    delete.setOnAction(event -> deleteDirectory(path.toFile()));

                    contextMenu.getItems().addAll(createFile, createFolder, new SeparatorMenuItem(), cut, copy, paste, new SeparatorMenuItem(), rename, new SeparatorMenuItem(), delete);
                    setContextMenu(contextMenu);
                }
            }
        });

        Autosave saver = new SaveTabsProcess(this);
        saver.start();
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
