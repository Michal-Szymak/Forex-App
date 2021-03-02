package com.infoshareacademy.forex_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Value("${default_filename}")
    private String defaultFilename;

    public String getDefaultFilename() {
        return defaultFilename;
    }

    public void setDefaultFilename(String defaultFilename) {
        this.defaultFilename = defaultFilename;
    }

}
