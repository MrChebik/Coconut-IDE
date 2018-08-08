package ru.mrchebik.language.java.command;

import ru.mrchebik.binaries.BinariesType;
import ru.mrchebik.command.Command;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.project.Project;

import java.nio.file.Paths;

public class JavaCommand extends Command {
    public String getCompile() {
        String compile = binaries.getBinary(BinariesType.COMPILE);
        return compile + " -d " + Project.pathOut.toString() + " " + FileHelper.getStructure();
    }

    public String getRun() {
        String run = binaries.getBinary(BinariesType.RUN);
        return run + " -cp " + Project.pathOut.toString() + " " + FileHelper.getPackageOfRunnable(Paths.get(""));
    }
}
