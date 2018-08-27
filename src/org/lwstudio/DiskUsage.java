package org.lwstudio;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DiskUsage {
    static void printFileStore(FileStore fileStore) throws IOException {
        long toGB = 1024 * 1024 * 1024;
        long total = fileStore.getTotalSpace() / toGB;
        long used = fileStore.getTotalSpace() / toGB - fileStore.getUnallocatedSpace() / toGB;
        long available = fileStore.getUsableSpace() / toGB;

        System.out.format(
                "%-20s %8d(GB) %8d(GB) %8d(GB)\n",
                fileStore.toString(), total, used, available
        );
    }

    public static void main(String[] args) throws IOException {
        System.out.format(
                "%-20s %12s %12s %12s\n",
                "FileSystem", "total", "used", "available"
        );

        if (args.length == 0) {
            for (FileStore store : FileSystems.getDefault().getFileStores()) {
                printFileStore(store);
            }
        } else {
            for (String file : args) {
                FileStore store = Files.getFileStore(Paths.get(file));
                printFileStore(store);
            }
        }
    }
}
