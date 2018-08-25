package ru.mrchebik.plugin.debug.ram.linux;

import ru.mrchebik.ide.IdeProcess;
import ru.mrchebik.plugin.debug.os.OsPluginDebug;
import ru.mrchebik.plugin.debug.os.OsPluginDebugWrapper;

public class LinuxPluginDebugRam extends OsPluginDebug implements OsPluginDebugWrapper {
    @Override
    public String[] getCommand() {
        return new String[]{"ps", "-q", IdeProcess.pid, "-o", "rss"};
    }

    @Override
    public String computeOutput(StringBuilder input) {
        return input.substring(
                input.indexOf("\n") + 1,
                input.length() - 1);
    }
}
