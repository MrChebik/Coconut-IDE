package ru.mrchebik.plugin.debug.cpu;

import javafx.scene.control.Label;
import ru.mrchebik.plugin.PluginWrapper;
import ru.mrchebik.plugin.debug.PluginDebug;
import ru.mrchebik.plugin.debug.cpu.linux.LinuxPluginDebugCpu;
import ru.mrchebik.plugin.debug.os.OsPluginDebug;

public class PluginDebugCpu extends PluginDebug implements PluginWrapper {
    public PluginDebugCpu(Label label) {
        super(label);

        name = "CPU";
        measurement = "%";
        updateMs = 2000;
        osPluginDebugWrapper = OsPluginDebug.getOs(
                new LinuxPluginDebugCpu());

        super.init();
    }
}
