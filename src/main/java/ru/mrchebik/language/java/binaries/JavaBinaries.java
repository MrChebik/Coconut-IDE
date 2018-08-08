package ru.mrchebik.language.java.binaries;

import ru.mrchebik.binaries.Binaries;
import ru.mrchebik.binaries.BinariesType;
import ru.mrchebik.binaries.BinariesWrapper;
import ru.mrchebik.settings.PropertyCollector;

import java.io.File;

public class JavaBinaries extends Binaries implements BinariesWrapper {
    public JavaBinaries() {
        compile = setBinary("javac");
        run = setBinary("java");
    }

    @Override
    public String setBinary(String bin) {
        String pathJdk = PropertyCollector.getProperty("jdk");

        return pathJdk == null ?
                    bin
                :
                    pathJdk + File.separator + "bin" + File.separator + bin;
    }

    @Override
    public String getBinary(BinariesType type) {
        String suffix = System.getProperty("os.name").contains("Windows") ?
                    ".exe"
                :
                    "";
        String binary = BinariesType.COMPILE.equals(type) ?
                    compile
                :
                    run;

        return binary + suffix;
    }
}
