package gmail.fopypvp174.cmloja.config;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfig extends Config {

    public MessageConfig(JavaPlugin plugin, String fileName, String defaultsName) {
        super(plugin, fileName, defaultsName);
    }

    public String message(String string, int itens, String money, OfflinePlayer target) {
        return ChatColor.translateAlternateColorCodes('&', getString(string).replace("%i", String.valueOf(itens)).replace("%m", money != null ? money : "").replace("%p", target != null ? target.getPlayer().getDisplayName() : ""));
    }
}