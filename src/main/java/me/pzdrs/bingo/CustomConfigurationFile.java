package me.pzdrs.bingo;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomConfigurationFile {
    private JavaPlugin plugin;
    private String fileName;
    private File file;
    private FileConfiguration fileConfiguration;

    public CustomConfigurationFile(JavaPlugin plugin, String fileName, boolean replace) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder(), fileName);
        createFile(replace);
        reload();
    }

    private void createFile(boolean replace) {
        if (file.exists()) return;
        try {
            file.getParentFile().createNewFile();
            plugin.saveResource(fileName, replace);
            plugin.getLogger().info("Successfully created file " + fileName + "!");
        } catch (IOException e) {
            plugin.getLogger().severe("Couldn't create file " + fileName + "!");
        }
    }

    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Couldn't save file " + fileName + "!");
        }
    }
}
