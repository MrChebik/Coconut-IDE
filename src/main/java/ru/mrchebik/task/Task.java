package ru.mrchebik.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Task extends Thread {
    @Getter
    private String command;

    public Task(Runnable runnable) {
        super(runnable);
    }

    public Task(Runnable runnable, String command) {
        super(runnable);

        this.command = command;
    }
}
