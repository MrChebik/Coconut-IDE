package ru.mrchebik.task;

import ru.mrchebik.command.CommandType;
import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.io.ErrorProcess;

public class TaskHelper {
    public static Task makeTask(CommandType type) {
        return new Task(() -> ExecutorCommand.execute(type.toString()));
    }

    public static void doTask(CommandType type) {
        new Task(() -> ExecutorCommand.execute(type.toString())).start();
    }

    public static void chain(Task... tasks) {
        new Task(() -> {
            for (int i = 0; i < tasks.length; i++) {
                try {
                    tasks[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!ErrorProcess.isWasError()) {
                    ExecutorCommand.execute(tasks[i].getCommand());
                }
            }
        }).start();
    }
}
