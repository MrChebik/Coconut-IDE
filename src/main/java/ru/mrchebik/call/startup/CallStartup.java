package ru.mrchebik.call.startup;

public abstract class CallStartup implements CallStartupWrapper {
    public void callNewProject() {
        throw new UnsupportedOperationException();
    }

    public void callSetupHome() {
        throw new UnsupportedOperationException();
    }

    public boolean isCorrectHome() {
        throw new UnsupportedOperationException();
    }
}
