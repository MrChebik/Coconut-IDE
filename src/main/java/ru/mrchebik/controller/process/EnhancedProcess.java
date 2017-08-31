package ru.mrchebik.controller.process;

import javafx.application.Platform;
import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.view.WorkStation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by mrchebik on 8/29/17.
 */
public class EnhancedProcess {
    private String[] commands;

    public EnhancedProcess(String... commands) {
        this.commands = commands;
    }

    public void start() {
        System.out.println(Arrays.toString(commands));

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        Process process = null;

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (process != null) {
            WorkStationController controller = WorkStation.getFxmlLoader().getController();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder lines = new StringBuilder();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    lines.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> controller.setOutText(lines.toString()));

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(controller::loadTree);
        }
    }
}
