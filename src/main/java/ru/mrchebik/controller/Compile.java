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
public class Compile {
    public static void start() {
        findAllJava(new File(Project.getPathSource()));

        new EnhancedProcess("javac",
                /*"-d", Project.getPathOut(),*/
                "@" + Project.getPathOut() + File.separator + "option.txt",
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
            String[] paths = Arrays.stream(files).map(File::getPath).toArray(String[]::new);

            Save.start(sourceNew.toPath(), "-d " + Project.getPathOut() + "\n" + String.join("\n", String.join("\n", paths)));
        }

        File option = new File(Project.getPathOut() + File.separator + "option.txt");
        try {
            option.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Save.start(option.toPath(), "-d " + Project.getPathOut() + "\n-sourcepath " + Project.getPathSource());
    }
}
