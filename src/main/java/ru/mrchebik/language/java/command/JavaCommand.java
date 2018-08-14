package ru.mrchebik.language.java.command;

import ru.mrchebik.binaries.BinariesType;
import ru.mrchebik.command.Command;
import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.language.Language;
import ru.mrchebik.project.Project;

import java.nio.file.Path;

public class JavaCommand extends Command implements CommandWrapper {
    public String getCompile() {
        String compile = Language.binaries.getBinary(BinariesType.COMPILE);
        return compile + " -d " + Project.pathOut.toString() + " " + FileHelper.getStructure();
    }

    public String getRun(Path path) {
        String run = Language.binaries.getBinary(BinariesType.RUN);
        return run + " -cp " + Project.pathOut.toString() + " " + FileHelper.getPackageOfRunnable(path);
    }
}
