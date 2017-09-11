package ru.mrchebik.controller.javafx.updater;

import com.google.inject.Inject;
import ru.mrchebik.controller.javafx.updater.tab.TabUpdater;
import ru.mrchebik.controller.javafx.updater.tree.TreeUpdater;
import ru.mrchebik.model.project.Project;

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
    private Path toRename;

    @Inject
    private Project project;

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

                        if (ENTRY_CREATE == kind) {
                            boolean isRoot = Objects.equals(project.getPath(), toRename);

                            if (toRename != null) {
                                updateCorePathIfRenameTo(newPath);

                                TabUpdater.updateTabs(newPath);
                            }

                            if (events.size() == 1) {
                                TreeUpdater.createObject(newPath, isRoot);
                            } else {
                                TreeUpdater.updateObject(toRename, newPath);
                            }

                            if (toRename != null) {
                                toRename = null;
                            }
                        } else if (ENTRY_DELETE == kind) {
                            if (events.size() == 1) {
                                TreeUpdater.removeObject(newPath);
                                TabUpdater.updateTabs(newPath);
                            }

                            setRenameToIfCorePath(newPath);
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

    private void updateCorePathIfRenameTo(Path path) {
        if (Objects.equals(toRename, project.getPath())) {
            int indexLastSlash = toRename.toString().lastIndexOf(File.separator);
            String name = toRename.toString().substring(indexLastSlash);

            project.setName(name);
            project.setPath(path);
        }
        if (Objects.equals(toRename, project.getPathSource())) {
            project.setPathSource(path);
        }
        if (Objects.equals(toRename, project.getPathOut())) {
            project.setPathOut(path);
        }
    }

    private void setRenameToIfCorePath(Path path) {
        if (Objects.equals(path, project.getPath()) ||
                Objects.equals(path, project.getPathSource()) ||
                Objects.equals(path, project.getPathOut())) {
            toRename = path;
        }
    }
}
