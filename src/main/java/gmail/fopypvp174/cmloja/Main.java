package gmail.fopypvp174.cmloja;

import gmail.fopypvp174.cmloja.api.Utilidades;
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

public class Main extends JavaPlugin {

    private JavaPlugin plugin;
    private LojaConfig loja;
    private MessageConfig messageConfig;
    private Economy econ;
    private Utilidades utilidades;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getLogger().info(String.format("[%s] Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupVault();
        plugin = this;
        loja = new LojaConfig(this, "itens.yml", "itens.yml");
        messageConfig = new MessageConfig(this, "configurar.yml", "configurar.yml");
        utilidades = new Utilidades();
        getServer().getPluginManager().registerEvents(new EventoCriar(), plugin);
        getServer().getPluginManager().registerEvents(new EventoComprar(), plugin);
        getServer().getPluginManager().registerEvents(new EventoVender(), plugin);
        getCommand("geraritem").setExecutor(new GerarItem());
    }

    public void setupVault() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
    }

    @Override
    public final void onDisable() {
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            loja.save();
            messageConfig.save();
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Utilidades getUtilidades() {
        return utilidades;
    }

    public LojaConfig getLoja() {
        return loja;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public Economy getEcon() {
        return econ;
    }
}
