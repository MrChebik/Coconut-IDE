package ru.mrchebik.controller.actions.autosave.saver;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.mrchebik.controller.actions.ReadFile;
import ru.mrchebik.model.controller.actions.autosave.FutureFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by mrchebik on 9/3/17.
 */
public class SaveFileTest {
    private static Path path;

    @BeforeClass
    public static void beforeClass() throws IOException {
        String pathToTestDir = System.getProperty("user.home") + File.separator + "Coconut-Test";
        String pathToTestDirFile = pathToTestDir + File.separator + "test";
        path = Paths.get(pathToTestDirFile);

        File dir = new File(pathToTestDir);
        dir.mkdir();

        File file = new File(pathToTestDirFile);
        file.createNewFile();
    }

    @Test
    public void save() throws Exception {
        FutureFile futureFile = new FutureFile(path, "Hello World!\nHello World");

        SaveFile saver = new SaveFile(futureFile);
        saver.save();

        assertEquals("Hello World!\nHello World", ReadFile.readFile(path));
    }
}