package ru.mrchebik.controller.javafx.updater.tab;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import ru.mrchebik.controller.actions.ReadFile;
import ru.mrchebik.controller.javafx.updater.tree.CustomTreeItem;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.view.CreatorFiles;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Created by mrchebik on 9/3/17.
 */
public class TabUpdater {
    private static TabPane tabPane;

    public static void updateTabs(Path newFile) {
        System.out.println("Old " + newFile);

        ObservableList<Tab> tabs = tabPane.getTabs();

        for (int i = 0; i < tabs.size(); i++) {
            Path path = (Path) tabs.get(i).getUserData();
            String name = path.getFileName().toString();

            System.out.println(newFile + File.separator + name + " // " + path.toString());
            if (!Objects.equals(newFile + File.separator + name, path.toString())) {
                String pathString = path.toString();
                String nameOfNewFile = Paths.get(newFile.toString()).getFileName().toString();

                if (pathString.startsWith(CreatorFiles.getPath().toString())) {
                    int indexOfDifference = 0;
                    StringBuilder nameOfDifference = new StringBuilder();

                    char[] inital = path.toString().toCharArray();
                    char[] rename = newFile.toString().toCharArray();

                    for (int j = 0; j < rename.length; j++) {
                        if (inital[j] != rename[j]) {
                            if (inital[j] != File.separator.charAt(0)) {
                                indexOfDifference = path.toString().substring(0, j).lastIndexOf(File.separator.charAt(0)) + 1;
                            } else {
                                indexOfDifference = j;
                            }
                            break;
                        }
                    }

                    for (int j = indexOfDifference; j < path.toString().length(); j++) {
                        if (inital[j] == File.separator.charAt(0)) {
                            break;
                        } else {
                            nameOfDifference.append(inital[j]);
                        }
                    }

                    String firstHalf = newFile.toString().substring(0, indexOfDifference);
                    String secondHalf = path.toString().substring(indexOfDifference + nameOfDifference.length());
                    Path newPath = Paths.get(firstHalf + nameOfNewFile + secondHalf);

                    tabPane.getTabs().get(i).setUserData(newPath);

                    System.out.println("New " + newPath);
                }
            }
        }
    }

    public static void addObjectToTab(CustomTreeItem item) {
        Tab tab = new Tab();
        TextArea code = new TextArea();

        Path path = item.getValue();

        String text = ReadFile.readFile(path);
        code.setText(text);

        tab.setText(item.getValue().getFileName().toString());
        tab.setGraphic(new ImageView(CustomIcons.getFileImage()));
        tab.setUserData(item.getValue());
        tab.setContent(code);

        tabPane.getTabs().add(tab);
    }

    public static void setTabPane(TabPane tabPane) {
        TabUpdater.tabPane = tabPane;
    }
}
