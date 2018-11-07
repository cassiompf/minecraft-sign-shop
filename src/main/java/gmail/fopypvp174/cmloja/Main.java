package gmail.fopypvp174.cmloja;

import gmail.fopypvp174.cmloja.cmds.GerarItem;
import gmail.fopypvp174.cmloja.config.LojaConfig;
import gmail.fopypvp174.cmloja.config.MessageConfig;
import gmail.fopypvp174.cmloja.listeners.EventoComprar;
import gmail.fopypvp174.cmloja.listeners.EventoCriar;
import gmail.fopypvp174.cmloja.listeners.EventoVender;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static JavaPlugin plugin;
    public static LojaConfig loja;
    public static MessageConfig messageConfig;
    public static Economy econ;

    @Override
    public final void onEnable() {
        if (!setupEconomy() ) {
            Bukkit.getLogger().info(String.format("[%s] Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;
        loja = new LojaConfig(this, "itens.yml", "itens.yml");
        messageConfig = new MessageConfig(this, "configurar.yml", "configurar.yml");
        getServer().getPluginManager().registerEvents(new EventoCriar(), plugin);
        getServer().getPluginManager().registerEvents(new EventoComprar(), plugin);
        getServer().getPluginManager().registerEvents(new EventoVender(), plugin);
        getCommand("geraritem").setExecutor(new GerarItem());
    }

    @Override
    public final void onDisable() {
        if (setupEconomy()) {
            loja.save();
            messageConfig.save();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
