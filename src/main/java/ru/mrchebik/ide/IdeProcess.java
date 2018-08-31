package ru.mrchebik.ide;

import lombok.AllArgsConstructor;

import java.lang.management.ManagementFactory;

@AllArgsConstructor
public enum IdeProcess {
    PID(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

    public String pid;
}
