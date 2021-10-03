package me.nuymakstone.hrainac.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.logging.Logger;

public class BukkitConfigurationUtil implements IConfigurationUtil {
    private static final String DATA_FOLDER_PLACEHOLDER = "%datafolder%";
    private final String dataFolderPath;
    private final String prefix;
    private final Logger logger;
    private final Plugin plugin;
    private final BukkitScheduler scheduler;
    private final ClassLoader classLoader;

    public BukkitConfigurationUtil(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        this.logger = plugin.getLogger();
        this.classLoader = plugin.getClass().getClassLoader();
        this.dataFolderPath = plugin.getDataFolder().toString();
        this.prefix = "[" + plugin.getName() + "]";
    }

    private void createParentFolder(File file) {
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
        }

    }

    public IConfiguration get(String path) {
        File file = new File(path.replace("%datafolder%", this.dataFolderPath));
        return file.exists() ? new BukkitConfiguration(YamlConfiguration.loadConfiguration(file)) : new BukkitConfiguration(new YamlConfiguration());
    }

    public void create(String rawPath, String resourcePath) {
        String path = rawPath.replace("%datafolder%", this.dataFolderPath);

        try {
            File configFile = new File(path);
            if (!configFile.exists()) {
                InputStream inputStream = this.classLoader.getResourceAsStream(resourcePath);
                this.createParentFolder(configFile);
                if (inputStream != null) {
                    Files.copy(inputStream, configFile.toPath(), new CopyOption[0]);
                } else {
                    configFile.createNewFile();
                }
            }
        } catch (IOException var6) {
            this.logger.info(this.prefix + " Unable to create '" + path + "'!");
        }

    }

    public void save(IConfiguration configuration, String rawPath) {
        String path = rawPath.replace("%datafolder%", this.dataFolderPath);
        this.scheduler.runTaskAsynchronously(this.plugin, () -> {
            try {
                ((YamlConfiguration)configuration.getObject()).save(path);
                this.logger.info(this.prefix + " File '" + path + "' has been saved!");
            } catch (IOException var4) {
                this.logger.info(this.prefix + " Unable to save '" + path + "'!");
            }

        });
    }

    public void delete(String rawPath) {
        String path = rawPath.replace("%datafolder%", this.dataFolderPath);
        this.scheduler.runTaskAsynchronously(this.plugin, () -> {
            try {
                Files.delete((new File(path)).toPath());
                this.logger.info(this.prefix + " File '" + path + "' has been removed!");
            } catch (IOException var3) {
                this.logger.info(this.prefix + " Unable to remove '" + path + "'!");
            }

        });
    }
}

