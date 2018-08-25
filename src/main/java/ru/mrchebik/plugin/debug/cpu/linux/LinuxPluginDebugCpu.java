package ru.mrchebik.plugin.debug.cpu.linux;

import ru.mrchebik.ide.IdeProcess;
import ru.mrchebik.plugin.debug.cpu.UtimeTotalTime;
import ru.mrchebik.plugin.debug.os.OsPluginDebug;
import ru.mrchebik.plugin.debug.os.OsPluginDebugWrapper;

import java.util.Arrays;

public class LinuxPluginDebugCpu extends OsPluginDebug implements OsPluginDebugWrapper {
    @Override
    public String[] getCommand() {
        return new String[]{"bash", "-c", "cat /proc/" + IdeProcess.pid + "/stat | awk '{print $14}' && "
                + "cat /proc/stat | sed -n 1p"};//"bash", "-c", "top -p " + IdeProcess.pid + " -b n1 | sed -n 8p | awk '{print $9}'" };
    }

    @Override
    public UtimeTotalTime computeOutput(StringBuilder input) {
        int enter = input.indexOf("\n");

        String totalTimes = input.substring(enter + 1, input.lastIndexOf("\n"));
        totalTimes = totalTimes.substring(totalTimes.indexOf(" ") + 2);
        String[] totalTimesItems = totalTimes.split(" ");

        int utime = Integer.parseInt(input.substring(0, enter));
        int total_time = Arrays.stream(totalTimesItems)
                .mapToInt(Integer::parseInt)
                .sum();

        return new UtimeTotalTime(utime, total_time);
    }
}
