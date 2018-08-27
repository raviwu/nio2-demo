package org.lwstudio;

import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

class MyWatchService implements Runnable {
    private WatchService myWatcher;
    public MyWatchService(WatchService myWatcher) {
        this.myWatcher = myWatcher;
    }

    @Override
    public void run() {
        try {
            WatchKey key = myWatcher.take();
            while (key != null) {
                for (WatchEvent event : key.pollEvents()) {
                    System.out.printf(
                            "Received event: %s for file: %s\n",
                            event.kind(), event.context()
                    );
                }
                key.reset();
                key = myWatcher.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class WatchServiceDemo {
    final static String DIRECTORY_TO_WATCH = "C:\\Users\\Ravi.Wu.TDC\\projects\\nio2-demo";
    public static void main(String[] args) throws Exception {
        Path watchPath = Paths.get(DIRECTORY_TO_WATCH);
        if (Files.exists(watchPath) == false) {
            Files.createDirectories(watchPath);
        }
        WatchService myWatcher = watchPath.getFileSystem().newWatchService();
        MyWatchService fileWatcher = new MyWatchService(myWatcher);
        Thread thread = new Thread(fileWatcher);
        thread.start();

        // register a file
        watchPath.register(myWatcher,
                ENTRY_CREATE,
                ENTRY_MODIFY,
                ENTRY_DELETE);
        thread.join();
    }
}