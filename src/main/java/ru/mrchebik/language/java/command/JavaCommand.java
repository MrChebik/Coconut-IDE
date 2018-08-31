package ru.mrchebik.language.java.command;

import ru.mrchebik.binaries.BinariesType;
import ru.mrchebik.command.Command;
import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.language.Language;
import ru.mrchebik.project.Project;
import ru.mrchebik.util.FileUtil;

import java.nio.file.Path;

public class JavaCommand extends Command implements CommandWrapper {
    public String getCompile() {
        var compile = Language.binaries.getBinary(BinariesType.COMPILE);
        return compile + " -d " + Project.pathOut.toString() + " " + FileUtil.getStructure();
    }

    public String getRun(Path path) {
        var run = Language.binaries.getBinary(BinariesType.RUN);
        return run + " -cp " + Project.pathOut.toString() + " " + FileUtil.getPackageOfRunnable(path);
    }
}
