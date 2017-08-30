package ru.mrchebik.controller.javafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.mrchebik.controller.Compile;
import ru.mrchebik.controller.Run;
import ru.mrchebik.controller.Save;
import ru.mrchebik.controller.process.SaveProcess;
import ru.mrchebik.model.Project;
import ru.mrchebik.view.WorkStation;
import ru.mrchebik.view.treeview.CustomIcons;
import ru.mrchebik.view.treeview.FilePathTreeItem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/29/17.
 */
public class WorkStationController implements Initializable {
    @FXML private TextArea code;
    @FXML private TextArea out;
    @FXML private Button compile;
    @FXML private Button run;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TabPane tabPane;

    private SaveProcess saveProcess;

    @FXML private void handleRunProject() {
        new Thread(() -> {
            out.setText("");

            WorkStationController controller = WorkStation.getFxmlLoader().getController();
            ObservableList<Tab> tabs = controller.getTabs();

            if (tabs.size() > 0) {
                TextArea textArea = (TextArea) tabs.filtered(e -> e.getText().equals("Main.java")).get(0).getContent();

                Save.start(textArea.getText());
            }

            Run.start();
        }).start();
    }

    @FXML private void handleCompileProject() {
        new Thread(() -> {
            out.setText("");

            Compile.start();
        }).start();
    }

    @FXML
    private void handleDoubleClick(MouseEvent e) throws IOException {
        if (e.getClickCount() == 2) {
            FilePathTreeItem item = (FilePathTreeItem) treeView.getSelectionModel().getSelectedItem();

            if (!item.isDirectory()) {
                Tab tabdata = new Tab();
                TextArea code = new TextArea();

                final String[] lines = {""};
                Files.readAllLines(new File(item.getFullPath()).toPath())
                        .forEach(line -> lines[0] += line + "\n");
                code.setText(lines[0]);

                tabdata.setText(item.getValue());
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
        TreeItem<String> rootNode = new TreeItem<>(Project.getName(), new ImageView(CustomIcons.folderExpandImage));
        rootNode.setExpanded(true);

        Arrays.stream(new File(Project.getPath()).listFiles()).forEach(e -> {
            rootNode.getChildren().add(new FilePathTreeItem(e.toPath()));
        });

        treeView.setRoot(rootNode);

        VBox.setVgrow(treeView, Priority.ALWAYS);

        saveProcess = new SaveProcess();
        saveProcess.start();
    }
}
