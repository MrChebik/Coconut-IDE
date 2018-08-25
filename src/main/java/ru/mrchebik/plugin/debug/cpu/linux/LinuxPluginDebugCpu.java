package ru.mrchebik.plugin.debug.cpu.linux;

import ru.mrchebik.ide.IdeProcess;
import ru.mrchebik.plugin.debug.os.OsPluginDebug;
import ru.mrchebik.plugin.debug.os.OsPluginDebugWrapper;

public class LinuxPluginDebugCpu extends OsPluginDebug implements OsPluginDebugWrapper {
    @Override
    public String[] getCommand() {
        return new String[]{"ps", "-q", IdeProcess.pid, "-o", "%cpu"};
    }

    @Override
    public String computeOutput(StringBuilder input) {
        return input.substring(
                input.indexOf("\n") + 1,
                input.length() - 1);
    }
}
