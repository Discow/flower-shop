package com.example.flowershop.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="spring.datasource")
public class CustomPropertiesConfiguration {
    private String database;
    private String backupPath;
    private String installPath;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }

    public String getInstallPath() {
        return installPath;
    }

    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }

    @Override
    public String toString() {
        return "CustomPropertiesConfiguration{" +
                "database='" + database + '\'' +
                ", backupPath='" + backupPath + '\'' +
                ", installPath='" + installPath + '\'' +
                '}';
    }
}
