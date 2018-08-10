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
            for (var task : tasks) {
                try {
                    task.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!ErrorProcess.isWasError())
                    ExecutorCommand.execute(task.getCommand());
            }
        }).start();
    }
}
