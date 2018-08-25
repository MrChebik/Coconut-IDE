package ru.mrchebik.plugin;

public abstract class Plugin implements PluginWrapper {
    protected String name;

    @Override
    public void start() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void complete() {
        throw new UnsupportedOperationException();
    }
}
