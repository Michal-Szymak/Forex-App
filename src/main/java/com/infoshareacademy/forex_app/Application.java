package com.infoshareacademy.forex_app;

import com.infoshareacademy.forex_app.model.DirectoryWatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {

    private final DirectoryWatcher directoryWatcher;

    public Application(DirectoryWatcher directoryWatcher) {
        this.directoryWatcher = directoryWatcher;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        directoryWatcher.watchDirectory();
    }

}
