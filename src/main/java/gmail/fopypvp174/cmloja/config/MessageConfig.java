package gmail.fopypvp174.cmloja.config;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public final class MessageConfig extends Config {

    public MessageConfig(JavaPlugin plugin, String fileName, String defaultsName) {
        super(plugin, fileName, defaultsName);
    }

    public String message(String string) {
        return ChatColor.translateAlternateColorCodes('&', getString(string));
    }

    public String message(String string, String money) {
        return ChatColor.translateAlternateColorCodes('&', getString(string).replace("%m", money != null ? money : ""));
    }

    public String message(String string, Integer itemQuantia) {
        return ChatColor.translateAlternateColorCodes('&',
                getString(string).replace("%i", itemQuantia != null ? itemQuantia.toString() : ""));
    }

    public String message(String string, Integer itemQuantia, String money) {
        return ChatColor.translateAlternateColorCodes('&',
                getString(string).replace("%i", itemQuantia != null ? itemQuantia.toString() : "")
                        .replace("%m", money != null ? money : ""));
    }

    public String message(String string, Integer itemQuantia, String money, OfflinePlayer target) {
        return ChatColor.translateAlternateColorCodes('&',
                getString(string).replace("%i", itemQuantia != null ? itemQuantia.toString() : "")
                        .replace("%m", money != null ? money : "")
                        .replace("%p", target != null ? target.getName() : ""));
    }
}