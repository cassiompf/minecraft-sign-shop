package gmail.fopypvp174.cmloja.configurations;

import gmail.fopypvp174.cmloja.CmLoja;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class Config {

    private FileConfiguration fileConfiguration;
    private File file;
    private CmLoja plugin;
    private String fileName;

    public Config(CmLoja plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        init();
    }

    private void init() {
        file = new File(plugin.getDataFolder(), fileName);
        plugin.saveResource(fileName, false);
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public final void saveConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void reloadConfig() {
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final FileConfiguration getCustomConfig() {
        return fileConfiguration;
    }

}
