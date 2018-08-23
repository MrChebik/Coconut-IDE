package ru.mrchebik.task;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Task extends Thread {
    Task(Runnable runnable) {
        super(runnable);
    }
}
