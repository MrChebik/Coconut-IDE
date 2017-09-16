package ru.mrchebik.gui.updater;

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
    @NonNull private TabUpdater tabUpdater;
    @NonNull private TreeUpdater treeUpdater;

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

            takedEvent(key);

            if (!key.reset())
                break;
        }
    }

    private void takedEvent(WatchKey key) {
        List<WatchEvent<?>> events = key.pollEvents();
        for (WatchEvent<?> watchEvent : events) {
            defineEvent(key, watchEvent, events.size());
        }
    }

    private void defineEvent(WatchKey key, WatchEvent<?> watchEvent, int size) {
        WatchEvent.Kind<?> kind = watchEvent.kind();

        if (OVERFLOW != kind) {
            Path newPath = ((Path) key.watchable())
                    .resolve(((WatchEvent<Path>) watchEvent)
                    .context());

            if (ENTRY_CREATE == kind)
                createAction(size, newPath);
            if (ENTRY_DELETE == kind)
                deleteAction(size, newPath);
        }
    }

    private void createAction(int size, Path path) {
        if (toRename != null) {
            updateCorePathIfRenameTo(path);
            tabUpdater.updateTabs(path, toRename);
        }

        if (size == 1)
            treeUpdater.createObject(path, isPath(toRename));
        else
            treeUpdater.updateObject(toRename, path);

        toRename = null;
    }

    private void deleteAction(int size, Path path) {
        if (size == 1) {
            treeUpdater.removeObject(path);
            tabUpdater.deleteTab(path);
        } else
            toRename = path;
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
