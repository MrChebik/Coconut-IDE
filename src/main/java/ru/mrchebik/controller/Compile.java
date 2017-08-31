package ru.mrchebik.controller;

import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.controller.utils.CustomFileFilter;
import ru.mrchebik.model.Project;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by mrchebik on 08.05.16.
 */
public class Compile extends Thread {
    @Override
    public void run() {
        findAllJava(new File(Project.getPathSource()));

        new EnhancedProcess("javac",
                "-d", Project.getPathOut(),
                "@" + Project.getPathOut() + File.separator + "source.txt").start();
    }

    private static void findAllJava(File source) {
        File sourceNew = new File(Project.getPathOut() + File.separator + "source.txt");
        try {
            sourceNew.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File[] files = source.listFiles(new CustomFileFilter());
        if (files != null) {
            Save save = new Save(sourceNew.toPath(),
                    String.join("\n", Arrays.stream(files).map(File::getPath).toArray(String[]::new)));
            save.start();
            try {
                save.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
