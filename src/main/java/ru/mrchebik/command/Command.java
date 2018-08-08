package ru.mrchebik.command;

import ru.mrchebik.binaries.Binaries;

public abstract class Command implements CommandWrapper {
    public static Binaries binaries;

    public String getCompile() {
        throw new UnsupportedOperationException();
    }

    public String getRun() {
        throw new UnsupportedOperationException();
    }
}
