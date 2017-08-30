package ru.mrchebik.view.treeview;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by mrchebik on 8/30/17.
 */
public class FilePathTreeItem extends TreeItem<String> {
    private String fullPath;

    private boolean isDirectory;

    public FilePathTreeItem(Path file) {
        super(file.toString());

        this.fullPath = file.toString();

        if (Files.isDirectory(file)) {
            this.isDirectory = true;
            this.setGraphic(new ImageView(CustomIcons.folderCollapseImage));
        } else {
            this.isDirectory = false;
            this.setGraphic(new ImageView(CustomIcons.fileImage));
        }

        if (!fullPath.endsWith(File.separator)) {
            String value = file.toString();

            int indexOf = value.lastIndexOf(File.separator);

            if (indexOf > 0) {
                this.setValue(value.substring(indexOf + 1));
            } else {
                this.setValue(value);
            }
        }

        this.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler() {
            @Override
            public void handle(Event e) {
                FilePathTreeItem source = (FilePathTreeItem) e.getSource();
                if (source.isDirectory() && source.isExpanded()) {
                    ImageView iv = (ImageView) source.getGraphic();
                    iv.setImage(CustomIcons.folderExpandImage);
                }
                try {
                    if (source.getChildren().isEmpty()) {
                        Path path = Paths.get(source.getFullPath());
                        BasicFileAttributes attribs = Files.readAttributes(path, BasicFileAttributes.class);
                        if (attribs.isDirectory()) {
                            DirectoryStream<Path> dir = Files.newDirectoryStream(path);
                            for (Path file : dir) {
                                FilePathTreeItem treeNode = new FilePathTreeItem(file);
                                source.getChildren().add(treeNode);
                            }
                        }
                    } else {

                    }
                } catch (IOException x) {
                    x.printStackTrace();
                }
            }
        });
    }

    public String getFullPath() {
        return fullPath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
