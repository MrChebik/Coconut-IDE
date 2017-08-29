package ru.mrchebik.controller;

import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.model.Project;
import ru.mrchebik.view.WorkStation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by mrchebik on 14.05.16.
 */
public class Save {
    public static void start() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(Project.getPath() + "/Main.java");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        assert writer != null;

        WorkStationController controller = WorkStation.getFxmlLoader().getController();

        writer.write(controller.getCodeText());
        writer.flush();
        writer.close();
    }
}
