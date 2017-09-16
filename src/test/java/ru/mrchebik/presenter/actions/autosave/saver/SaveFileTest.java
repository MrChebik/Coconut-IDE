package ru.mrchebik.presenter.actions.autosave.saver;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import ru.mrchebik.model.ExistFileToSave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by mrchebik on 9/3/17.
 */
public class SaveFileTest {
    private Path path;

    @Before
    public void setUp() throws IOException {
        Path testDir = Paths.get(System.getProperty("user.home"), "Coconut-Test");
        Path testFile = testDir.resolve("test");

        if (!Files.exists(testDir))
            Files.createDirectory(testDir);
        if (!Files.exists(testDir))
            Files.createFile(testFile);

        path = testFile;
    }

    @Test
    public void save() throws Exception {
        ExistFileToSave existFileToSave = new ExistFileToSave(path, "Hello World!\nHello World");
        existFileToSave.save();

        assertEquals("Hello World!\nHello World", getText(path));
    }

    @SneakyThrows(IOException.class)
    private String getText(Path path) {
        return Files.readAllLines(path).stream()
                .collect(Collectors.joining("\n"));
    }
}