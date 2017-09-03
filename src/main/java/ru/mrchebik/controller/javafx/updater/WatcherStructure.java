package ru.mrchebik.controller.javafx.updater;

import ru.mrchebik.controller.javafx.updater.tab.TabUpdater;
import ru.mrchebik.controller.javafx.updater.tree.TreeUpdater;
import ru.mrchebik.model.Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by mrchebik on 9/3/17.
 */
public class WatcherStructure extends Thread {
    private Path path;
    private String toRename;

    public WatcherStructure(Path path) {
        this.path = path;
    }

    @Override
    public void run() {
        watchDirectory();
    }

    private void watchDirectory() {
        FileSystem fs = FileSystems.getDefault();

        try (WatchService service = fs.newWatchService()) {
            path.register(service, ENTRY_CREATE, ENTRY_DELETE);

            WatchKey key;
            while (true) {
                key = service.take();

                WatchEvent.Kind<?> kind;
                List<WatchEvent<?>> events = key.pollEvents();
                for (WatchEvent<?> watchEvent : events) {
                    kind = watchEvent.kind();

                    if (OVERFLOW != kind) {
                        Path dir = (Path) key.watchable();
                        Path newPath = dir.resolve(((WatchEvent<Path>) watchEvent)
                                .context());
                        String newPathString = newPath.toString();

                        if (ENTRY_CREATE == kind) {
                            boolean isRoot = Objects.equals(Project.getPath(), toRename);

                            if (toRename != null) {
                                updateCorePathIfRenameTo(newPathString);

                                TabUpdater.updateTabs(newPathString);
                            }

                            if (events.size() == 1) {
                                TreeUpdater.createObject(newPath, isRoot);
                            } else {
                                TreeUpdater.updateObject(Paths.get(toRename), newPath);
                            }

                            if (toRename != null) {
                                toRename = null;
                            }
                        } else if (ENTRY_DELETE == kind) {
                            if (events.size() == 1) {
                                TreeUpdater.removeObject(newPath);
                                TabUpdater.updateTabs(newPathString);
                            }

                            setRenameToIfCorePath(newPathString);
                        }
                    }
                }

                if (!key.reset()) {
                    break;
                }
            }
        } catch (IOException | InterruptedException ioe) {
            ioe.printStackTrace();
        }
    }

    private void updateCorePathIfRenameTo(String path) {
        if (Objects.equals(toRename, Project.getPath())) {
            int indexLastSlash = toRename.lastIndexOf(File.separator);
            String name = toRename.substring(indexLastSlash);

            Project.setName(name);
            Project.setPath(path);
        }
        if (Objects.equals(toRename, Project.getPathSource())) {
            Project.setPathSource(path);
        }
        if (Objects.equals(toRename, Project.getPathOut())) {
            Project.setPathOut(path);
        }
        if (Objects.equals(toRename, Project.getPathOutListStructure())) {
            Project.setPathOutListStructure(path);
        }
    }

    private void setRenameToIfCorePath(String path) {
        if (Objects.equals(path, Project.getPath()) ||
                Objects.equals(path, Project.getPathSource()) ||
                Objects.equals(path, Project.getPathOut()) ||
                Objects.equals(path, Project.getPathOutListStructure())) {
            toRename = path;
        }
    }
}
