package ru.mrchebik.plugin.debug.os;

public interface OsPluginDebugWrapper {
    String[] getCommand();

    String computeOutput(StringBuilder input);

    boolean isSupported();
}
