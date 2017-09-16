package ru.mrchebik.gui.updater;

import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.mrchebik.gui.updater.tab.TabUpdater;
import ru.mrchebik.gui.updater.tree.TreeUpdater;
import ru.mrchebik.model.Project;

import java.nio.file.*;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by mrchebik on 9/3/17.
 */
@RequiredArgsConstructor
public class WatcherStructure extends Thread {
    @NonNull private Path path;
    @NonNull private Project project;
    @NonNull private TabPane tabPane;
    @NonNull private TreeView<Path> treeView;

    private Path toRename;

    @Override
    public void run() {
        watchDirectory();
    }

    @SneakyThrows
    private void watchDirectory() {
        @Cleanup WatchService service = FileSystems.getDefault().newWatchService();
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

                    TabUpdater tabUpdater = new TabUpdater(tabPane);
                    TreeUpdater treeUpdater = new TreeUpdater(project, tabPane, treeView);
                    if (ENTRY_CREATE == kind) {
                        if (toRename != null) {
                            updateCorePathIfRenameTo(newPath);
                            tabUpdater.updateTabs(newPath, toRename);
                        }

                        if (events.size() == 1)
                            treeUpdater.createObject(newPath, isPath(toRename));
                        else
                            treeUpdater.updateObject(toRename, newPath);

                        toRename = null;
                    }
                    if (ENTRY_DELETE == kind) {
                        if (events.size() == 1) {
                            treeUpdater.removeObject(newPath);
                            tabUpdater.updateTabs(newPath, toRename);
                        } else {
                            toRename = newPath;
                        }
                    }
                }
            }

            if (!key.reset()) {
                break;
            }
        }
    }

    private void updateCorePathIfRenameTo(Path path) {
        if (isPath(toRename)) {
            project.setName(path.getFileName().toString());
            project.setPath(path);
        }
        if (isPathOut(toRename))
            project.setPathSource(path);
        if (isPathSource(toRename))
            project.setPathOut(path);
    }

    private boolean isPath(Path path) {
        return Objects.equals(path, project.getPath());
    }

    private boolean isPathOut(Path path) {
        return Objects.equals(path, project.getPathOut());
    }

    private boolean isPathSource(Path path) {
        return Objects.equals(path, project.getPathSource());
    }
}
