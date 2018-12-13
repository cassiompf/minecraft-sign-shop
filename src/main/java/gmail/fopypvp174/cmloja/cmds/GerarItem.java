package gmail.fopypvp174.cmloja.cmds;

import gmail.fopypvp174.cmloja.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GerarItem implements CommandExecutor {

    private Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("geraritem")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("loja.geraritem")) {
                    ItemStack item = p.getItemInHand().clone();
                    item.setItemMeta(p.getItemInHand().getItemMeta());
                    item.setAmount(1);
                    ItemStack compararItem = new ItemStack(item.getType(), 1, item.getData().getData());
                    if (item.isSimilar(compararItem)) {
                        p.sendMessage(ChatColor.GREEN + "ID do item: (" + ChatColor.WHITE + item.getData().getItemTypeId() + ChatColor.GREEN + ":" + ChatColor.WHITE + item.getData().getData() + ChatColor.GREEN + ").");
                        return false;
                    }
                    if (plugin.getLoja().equalsItem(item)) {
                        p.sendMessage(ChatColor.RED + "Esse item já tem ID: (" + ChatColor.WHITE + plugin.getLoja().nameItem(item) + ChatColor.RED + ").");
                    } else {
                        plugin.getLoja().setItem(item);
                        p.sendMessage(ChatColor.GREEN + "ID do item: (" + ChatColor.WHITE + plugin.getLoja().nameItem(item) + ChatColor.GREEN + ").");
                    }
                } else {
                    p.sendMessage(plugin.getMessageConfig().message("mensagens.gerar_erro1", 0, null, null));
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Apenas jogadores podem usar esse comando");
            }
        }
        return false;
    }
}
