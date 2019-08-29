package gmail.fopypvp174.cmloja;

import gmail.fopypvp174.cmloja.commands.ItemGenerate;
import gmail.fopypvp174.cmloja.configurations.LojaConfig;
import gmail.fopypvp174.cmloja.configurations.MessageConfig;
import gmail.fopypvp174.cmloja.listeners.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public final class CmLoja extends JavaPlugin {
    private Runnable onDisable;
    private Economy economy = null;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        final LojaConfig loja = new LojaConfig(this, "itens.yml");
        final MessageConfig messageConfig = new MessageConfig(this, "configurar.yml");

        this.onDisable = () -> {
            loja.saveConfig();
            messageConfig.saveConfig();
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[cmLoja] desativado com sucesso!");
        };

        Bukkit.getPluginManager().registerEvents(new BuyChestEvent(economy, messageConfig, loja, this), this);
        Bukkit.getPluginManager().registerEvents(new BuySignEvent(economy, messageConfig, loja, this), this);
        Bukkit.getPluginManager().registerEvents(new CreateShopEvent(economy, messageConfig, loja, this), this);
        Bukkit.getPluginManager().registerEvents(new EventBreakShop(messageConfig, this), this);
        Bukkit.getPluginManager().registerEvents(new EventOpenChest(messageConfig, this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerShopEvent(messageConfig), this);
        Bukkit.getPluginManager().registerEvents(new SellChestShop(economy, messageConfig, loja, this), this);
        Bukkit.getPluginManager().registerEvents(new SellSignEvent(economy, messageConfig, this, loja), this);

        getCommand("geraritem").setExecutor(new ItemGenerate(loja));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[cmLoja] Plugin ativado com sucesso!");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[cmLoja] Autor: C4ssi0");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[cmLoja] GitHub: github.com/C4ssi0/cmLoja");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().
                getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    @Override
    public void onDisable() {
        onDisable.run();
    }
}