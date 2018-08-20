package ru.mrchebik.task;

import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.io.ErrorProcess;

public class TaskAction {
    public static Task makeTask(String command) {
        return new Task(() -> ExecutorCommand.execute(command));
    }

    public static void doTask(String command) {
        new Task(() -> ExecutorCommand.execute(command)).start();
    }

    public static void chain(Task... tasks) {
        new Task(() -> {
            for (var task : tasks) {
                try {
                    task.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!ErrorProcess.wasError)
                    task.run();
            }
        }).start();
    }
}
