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

    public InputProcess(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        WorkStationController controller = WorkStation.getFxmlLoader().getController();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                controller.setOutText(controller.getOutText() + line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
