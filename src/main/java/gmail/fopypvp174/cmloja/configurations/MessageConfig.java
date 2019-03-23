package gmail.fopypvp174.cmloja.configurations;

import gmail.fopypvp174.cmloja.CmLoja;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public final class MessageConfig extends Config {
    public MessageConfig(CmLoja plugin, String fileName) {
        super(plugin, fileName);
    }

    public String message(String string) {
        return ChatColor.translateAlternateColorCodes('&', getCustomConfig().getString(string));
    }

    public String message(String string, String money) {
        return ChatColor.translateAlternateColorCodes('&', getCustomConfig().getString(string).replace("%m", money != null ? money : ""));
    }

    public String message(String string, Integer itemQuantia) {
        return ChatColor.translateAlternateColorCodes('&',
                getCustomConfig().getString(string).replace("%i", itemQuantia != null ? itemQuantia.toString() : ""));
    }

    public String message(String string, Integer itemQuantia, String money) {
        return ChatColor.translateAlternateColorCodes('&',
                getCustomConfig().getString(string).replace("%i", itemQuantia != null ? itemQuantia.toString() : "")
                        .replace("%m", money != null ? money : ""));
    }

    public String message(String string, Integer itemQuantia, String money, OfflinePlayer target) {
        return ChatColor.translateAlternateColorCodes('&',
                getCustomConfig().getString(string).replace("%i", itemQuantia != null ? itemQuantia.toString() : "")
                        .replace("%m", money != null ? money : "")
                        .replace("%p", target != null ? target.getName() : ""));
    }
}