package com.infoshareacademy.forex_app.model;

import com.infoshareacademy.forex_app.service.ForexEntryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

@Component
@Slf4j
public class DirectoryWatcher {

    private final ForexEntryService service;

    public DirectoryWatcher(ForexEntryService service) {
        this.service = service;
    }

    public void watchDirectory() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(System.getProperty("user.dir"), "data");
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        WatchKey watchKey;

        while ((watchKey = watchService.take()) != null) {
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                if(service.checkFileForForexData(event.context().toString())) { log.info("Forex Data updated"); }
                watchKey.reset();
            }
        }
    }

}
