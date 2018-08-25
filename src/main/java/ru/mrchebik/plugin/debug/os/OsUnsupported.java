package ru.mrchebik.plugin.debug.os;

public class OsUnsupported extends OsPluginDebug implements OsPluginDebugWrapper {
    @Override
    public boolean isSupported() {
        return false;
    }
}
