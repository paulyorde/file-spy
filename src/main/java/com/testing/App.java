package com.testing;

import org.apache.tika.Tika;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class App
{
    private static final String FILE_TYPE = "text/csv";
    private static final String DIR = "/Users/origin/tmp";

    public static void main( String[] args ) throws Exception {
        Path dir = Paths.get(DIR);
        final Tika tika = new Tika();
        WatchService watchService = FileSystems.getDefault().newWatchService();
        dir.register(watchService, ENTRY_CREATE);

        WatchKey key;
        do {
             key = watchService.take();
             key.pollEvents().stream().filter(e -> {
             Path fileName = (Path)e.context();
             String type = tika.detect(fileName.toString());
                 return FILE_TYPE.equals(type);
             }).forEach(e -> System.out.printf("File found %s%n",e.context()));
        } while (key.reset());
    }

}
