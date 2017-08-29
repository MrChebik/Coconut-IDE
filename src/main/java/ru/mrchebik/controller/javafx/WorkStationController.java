package ru.mrchebik.controller.javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.mrchebik.controller.Compile;
import ru.mrchebik.controller.Run;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * Created by mrchebik on 8/29/17.
 */
public class WorkStationController implements Initializable {
    @FXML private TextArea code;
    @FXML private TextArea out;
    @FXML private Button compile;
    @FXML private Button run;

    @FXML private void handleRunProject() {
        Run.start();
    }

    @FXML private void handleCompileProject() {
        out.setText("");

        Compile.start();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final String[] text = {""};

        try {
            Files.readAllLines(Paths.get(WorkStationController.class.getResource("/example").getPath()), Charset.forName("UTF-8"))
                    .forEach(e -> text[0] += e + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        code.setText(text[0]);
    }
}
