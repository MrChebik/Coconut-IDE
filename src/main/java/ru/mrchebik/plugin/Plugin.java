package ru.mrchebik.plugin;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Plugin implements PluginWrapper {
    protected String pid;

    protected String name;
    protected String measurement;
    protected Thread thread;
    protected boolean isStop;
    protected AtomicBoolean isOpenIO;

    protected int updateMs;

    @Override
    public void start() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void complete() {
        isOpenIO.set(false);
        isStop = true;
        if (!thread.isInterrupted())
            thread.interrupt();
    }
}
