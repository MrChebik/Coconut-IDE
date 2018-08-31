package ru.mrchebik.task;

import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.io.ErrorProcess;

import java.nio.file.Path;
import java.util.Arrays;

public class TaskAction {
    public static Task makeTask(String command) {
        return new Task(() -> ExecutorCommand.execute(command));
    }

    public static void doTask(String command) {
        new Task(() -> ExecutorCommand.execute(command)).start();
    }

    /**
     * A new thread is started, where threads from the array are started in turn.
     *
     * @param tasks
     *        An array of threads.
     *
     * @see   ru.mrchebik.build.Build#run(Path)
     * @since 0.3.0-a
     */
    public static void chain(Task... tasks) {
        new Task(() -> {
            Arrays.stream(tasks)
                    .forEach(task -> {
                        try {
                            task.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!ErrorProcess.wasError) task.run();
                    });
            ErrorProcess.wasError = false;
        }).start();
    }
}
