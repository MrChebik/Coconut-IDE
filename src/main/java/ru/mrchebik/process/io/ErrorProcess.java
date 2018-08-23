package ru.mrchebik.process.io;

import java.io.InputStream;

public class ErrorProcess {
    public static boolean wasError;
    public static InputStream inputStream;

    public static void start() {
        var errorThread = new ErrorThread();
        errorThread.start();
    }
}

