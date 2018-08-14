package ru.mrchebik.command;

import java.nio.file.Path;

public abstract class Command implements CommandWrapper {
    public String getCompile() {
        throw new UnsupportedOperationException();
    }

    public String getRun(Path path) {
        throw new UnsupportedOperationException();
    }
}
