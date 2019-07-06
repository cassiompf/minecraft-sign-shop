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

    private LojaConfig loja;
    private MessageConfig messageConfig;
    private Economy econ;

    @Override
    public void onEnable() {
        if (!setupVault()) {
            Bukkit.getLogger().info(String.format("[%s] O Vault + plugin de economia não foram encontrados na pasta do servidor!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        } else {
            loja = new LojaConfig(this, "itens.yml");
            messageConfig = new MessageConfig(this, "configurar.yml");

            getServer().getPluginManager().registerEvents(new CreateShopEvent(this), this);

            getServer().getPluginManager().registerEvents(new BuySignEvent(this), this);
            getServer().getPluginManager().registerEvents(new BuyChestEvent(this), this);

            getServer().getPluginManager().registerEvents(new SellSignEvent(this), this);
            getServer().getPluginManager().registerEvents(new SellChestShop(this), this);

            getServer().getPluginManager().registerEvents(new PlayerShopEvent(this), this);

            getServer().getPluginManager().registerEvents(new EventOpenChest(this), this);
            getServer().getPluginManager().registerEvents(new EventBreakShop(this), this);

            getCommand("geraritem").setExecutor(new ItemGenerate(this));
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[cmLoja] Plugin ativado com sucesso!");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[cmLoja] Autor: C4ssi0");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[cmLoja] GitHub: github.com/C4ssi0/cmLoja");
        }
    }

    private boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            this.econ = (rsp == null ? null : rsp.getProvider());
        }
        return this.econ != null;
    }

    @Override
    public void onDisable() {
        if (setupVault()) {
            loja.saveConfig();
            messageConfig.saveConfig();
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[cmLoja] desativado com sucesso!");
        }
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
