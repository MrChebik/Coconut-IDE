package ru.mrchebik.controller.actions.autosave.saver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import ru.mrchebik.controller.actions.ReadFile;

import java.io.File;
import java.nio.file.Path;

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

        String path = System.getProperty("user.home") + File.separator + "Coconut-Test";

        File dir = new File(path);
        dir.mkdir();

        for (int i = 0; i < 2; i++) {
            Tab tab = new Tab();

            File file = new File(path + File.separator + "tab-test-" + i + ".txt");
            paths[i] = file.toPath();
            file.createNewFile();

            tab.setUserData(paths[i]);

            TextArea area = find("#area" + i);
            area.setText("Hello World " + i);
            tab.setContent(area);

            tabs.add(tab);
        }
    }

    @Test
    public void save() throws Exception {
        SaveTabs saver = new SaveTabs(tabs);
        saver.save();

        assertEquals("Hello World 0", ReadFile.readFile(paths[0]));
        assertEquals("Hello World 1", ReadFile.readFile(paths[1]));
    }

    @Override
    protected Parent getRootNode() {
        AnchorPane pane = new AnchorPane();
        TextArea area0 = new TextArea();
        area0.setId("area0");
        TextArea area1 = new TextArea();
        area1.setId("area1");
        pane.getChildren().addAll(area0, area1);

        return pane;
    }
}