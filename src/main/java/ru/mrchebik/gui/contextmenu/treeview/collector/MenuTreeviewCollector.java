package ru.mrchebik.gui.contextmenu.treeview.collector;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.ContextMenuEvent;
import ru.mrchebik.locale.Locale;

import java.nio.file.Path;

public class MenuTreeviewCollector {
    public static ContextMenu contextMenu;

    static MenuItem createFile;
    static MenuItem createFolder;
    static MenuItem copy;
    static MenuItem cut;
    static MenuItem paste;
    static MenuItem rename;
    static MenuItem delete;

    static {
        initItems();
        addItems();
    }

    private static void initItems() {
        contextMenu = new ContextMenu();

        createFile = new MenuItem(Locale.getProperty("create_file_menu", true));
        createFolder = new MenuItem(Locale.getProperty("create_folder_menu", true));
        copy = new MenuItem(Locale.getProperty("copy_menu", true));
        cut = new MenuItem(Locale.getProperty("cut_menu", true));
        paste = new MenuItem(Locale.getProperty("paste_menu", true));
        rename = new MenuItem(Locale.getProperty("rename_menu", true));
        delete = new MenuItem(Locale.getProperty("delete_menu", true));
    }

    private static void addItems() {
        contextMenu.getItems()
                .addAll(createFile, createFolder,
                        new SeparatorMenuItem(),
                        copy, cut, paste,
                        new SeparatorMenuItem(),
                        rename,
                        new SeparatorMenuItem(),
                        delete);
    }

    public static void handleRequest(ContextMenuEvent event, Path path) {
        MenuTreeviewCollectorAction.checkPath(path);
        MenuTreeviewCollectorAction.initListeners(path);

        contextMenu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
    }
}
