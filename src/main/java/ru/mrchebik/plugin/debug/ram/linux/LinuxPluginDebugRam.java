package ru.mrchebik.plugin.debug.ram.linux;

import ru.mrchebik.ide.IdeProcess;
import ru.mrchebik.plugin.debug.os.OsPluginDebug;
import ru.mrchebik.plugin.debug.os.OsPluginDebugWrapper;

public class LinuxPluginDebugRam extends OsPluginDebug implements OsPluginDebugWrapper {
    @Override
    public String[] getCommand() {
        return new String[]{"bash", "-c", "ps -q " + IdeProcess.PID + " -o rss | sed -n 2p && " +
                "top -p " + IdeProcess.PID + " -b n1 | sed -n 8p | awk '{print $7}'"};
    }

    @Override
    public String computeOutput(StringBuilder input) {
        int enter = input.indexOf("\n");

        int rss = Integer.parseInt(input.substring(0, enter));
        int shr = Integer.parseInt(input.substring(enter + 1));

        return String.valueOf(rss - shr);
    }
}
