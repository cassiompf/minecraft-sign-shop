package gmail.fopypvp174.cmloja;

import gmail.fopypvp174.cmloja.commands.ItemGenerate;
import gmail.fopypvp174.cmloja.configurations.LojaConfig;
import gmail.fopypvp174.cmloja.configurations.MessageConfig;
import gmail.fopypvp174.cmloja.listeners.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class CmLoja extends JavaPlugin {
    private Runnable onDisable;

    @Override
    public void onEnable() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        final ServicesManager servicesManager = Bukkit.getServicesManager();
        final Economy economy = setupEconomy(pluginManager, servicesManager);
        if (economy == null) {
            Bukkit.getLogger().info(String.format("[%s] O Vault + plugin de economia não foram encontrados na pasta do servidor!", getDescription().getName()));
            pluginManager.disablePlugin(this);
            return;
        }
        final LojaConfig loja = new LojaConfig(this, "itens.yml");
        final MessageConfig messageConfig = new MessageConfig(this, "configurar.yml");

        this.onDisable = () -> {
            loja.saveConfig();
            messageConfig.saveConfig();
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[cmLoja] desativado com sucesso!");
        };

        List.of(
                new CreateShopEvent(economy, messageConfig, loja),
                new BuySignEvent(economy, messageConfig, loja),
                new BuyChestEvent(economy, messageConfig, loja),
                new SellSignEvent(economy, messageConfig, loja),
                new SellChestShop(economy, messageConfig, loja),
                new PlayerShopEvent(messageConfig),
                new EventOpenChest(messageConfig),
                new EventBreakShop(messageConfig)
        ).forEach(it -> pluginManager.registerEvents(it, this));

        getCommand("geraritem").setExecutor(new ItemGenerate(loja));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[cmLoja] Plugin ativado com sucesso!");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[cmLoja] Autor: C4ssi0");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[cmLoja] GitHub: github.com/C4ssi0/cmLoja");
    }

    private Economy setupEconomy(PluginManager pluginManager, ServicesManager servicesManager) {
        if (pluginManager.getPlugin("Vault") == null) return null;
        final RegisteredServiceProvider<Economy> econProvider = servicesManager.getRegistration(Economy.class);
        if (econProvider == null) return null;
        return econProvider.getProvider();
    }

    @Override
    public void onDisable() {
        onDisable.run();
    }
}