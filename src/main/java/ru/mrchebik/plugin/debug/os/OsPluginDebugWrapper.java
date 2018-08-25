package ru.mrchebik.plugin.debug.os;

public interface OsPluginDebugWrapper {
    String[] getCommand();

    Object computeOutput(StringBuilder input);

    boolean isSupported();
}
