package ru.mrchebik.plugin.debug.ram;

import javafx.scene.control.Label;
import ru.mrchebik.plugin.PluginWrapper;
import ru.mrchebik.plugin.debug.PluginDebug;
import ru.mrchebik.plugin.debug.os.OsPluginDebug;
import ru.mrchebik.plugin.debug.ram.linux.LinuxPluginDebugRam;

public class PluginDebugRam extends PluginDebug implements PluginWrapper {
    public PluginDebugRam(Label label) {
        super(label);

        name = "RAM";
        measurement = "Mb";
        updateMs = 2000;
        osPluginDebugWrapper = OsPluginDebug.getOs(
                new LinuxPluginDebugRam());

        super.init();
    }
}
