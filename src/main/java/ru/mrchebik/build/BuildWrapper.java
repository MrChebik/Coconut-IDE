package ru.mrchebik.build;

import java.nio.file.Path;

public interface BuildWrapper {
    void compile();
    void run(Path path);
}
