package ru.mrchebik.ide;

import java.lang.management.ManagementFactory;

public class IdeProcess {
    public static String pid;

    static {
        pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
