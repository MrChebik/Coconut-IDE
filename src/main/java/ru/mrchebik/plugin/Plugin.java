package ru.mrchebik.plugin;

public abstract class Plugin implements PluginWrapper {
    protected String name;
    protected boolean isStop;

    @Override
    public void start() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void complete() {
        isStop = true;
    }
}
