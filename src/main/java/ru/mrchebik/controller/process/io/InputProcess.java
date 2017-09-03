package ru.mrchebik.controller.process.io;

import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.view.WorkStation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mrchebik on 8/31/17.
 */
public class InputProcess extends Thread {
    private InputStream inputStream;
    private WorkStationController controller;

    public InputProcess(InputStream inputStream) {
        this.inputStream = inputStream;
        this.controller = WorkStation.getFxmlLoader().getController();
    }

    @Override
    public void run() {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String previousLines = controller.getOutText();
                controller.setOutText(previousLines + line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
