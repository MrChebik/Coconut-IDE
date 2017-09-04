package ru.mrchebik.controller.actions.autosave.saver;

import org.junit.Before;
import org.junit.Test;
import ru.mrchebik.controller.actions.ReadFile;
import ru.mrchebik.model.controller.actions.autosave.ExistFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by mrchebik on 9/3/17.
 */
public class SaveFileTest {
    private ExistFile existFile;

    @Before
    public void setUp() throws IOException {
        String pathToTestDir = System.getProperty("user.home") + File.separator + "Coconut-Test";
        String pathToTestDirFile = pathToTestDir + File.separator + "test";

        File dir = new File(pathToTestDir);
        dir.mkdir();

        File file = new File(pathToTestDirFile);
        file.createNewFile();

        Path path = Paths.get(pathToTestDirFile);
        existFile = new ExistFile(path, "Hello World!\nHello World");
    }

    @Test
    public void save() throws Exception {
        SaveFile saver = new SaveFile(existFile);
        saver.save();

        assertEquals("Hello World!\nHello World", ReadFile.readFile(existFile.getPath()));
    }
}