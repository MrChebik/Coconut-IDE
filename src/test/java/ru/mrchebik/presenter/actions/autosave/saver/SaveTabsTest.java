package ru.mrchebik.presenter.actions.autosave.saver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import ru.mrchebik.process.SaveTabs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by mrchebik on 9/3/17.
 */
public class SaveTabsTest extends GuiTest {
    private ObservableList<Tab> tabs;

    private Path[] paths;

    @Before
    public void setUp() throws Exception {
        tabs = FXCollections.observableArrayList();

        paths = new Path[2];

        Path testDir = Paths.get(System.getProperty("user.home"), "Coconut-Test");

        Files.createDirectory(testDir);

        for (int i = 0; i < 2; i++) {
            Path file = testDir.resolve("tab-test-" + i + ".txt");
            Files.createFile(file);

            paths[i] = file;

            Tab tab = new Tab();
            tab.setUserData(file);

            TextArea area = find("#area" + i);
            area.setText("Hello World " + i);
            tab.setContent(area);

            tabs.add(tab);
        }
    }

    @Test
    public void save() throws Exception {
        SaveTabs saver = new SaveTabs(tabs);
        saver.run();

        assertEquals("Hello World 0", getText(paths[0]));
        assertEquals("Hello World 1", getText(paths[1]));
    }

    protected Parent getRootNode() {
        AnchorPane pane = new AnchorPane();
        TextArea area0 = new TextArea();
        area0.setId("area0");
        TextArea area1 = new TextArea();
        area1.setId("area1");
        pane.getChildren().addAll(area0, area1);

        return pane;
    }

    @SneakyThrows(IOException.class)
    private String getText(Path path) {
        return Files.readAllLines(path).stream()
                .collect(Collectors.joining("\n"));
    }
}