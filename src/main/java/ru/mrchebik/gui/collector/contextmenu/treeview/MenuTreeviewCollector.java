package ru.mrchebik.gui.collector.contextmenu.treeview;

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

        createFile = new MenuItem(Locale.CREATE_FILE_MENU);
        createFolder = new MenuItem(Locale.CREATE_FOLDER_MENU);
        copy = new MenuItem(Locale.COPY_MENU);
        cut = new MenuItem(Locale.CUT_MENU);
        paste = new MenuItem(Locale.PASTE_MENU);
        rename = new MenuItem(Locale.RENAME_BUTTON);
        delete = new MenuItem(Locale.DELETE_MENU);
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
        MenuTreeviewCollectorHelper.checkPath(path);
        MenuTreeviewCollectorHelper.initListeners(path);

        contextMenu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
    }
}
