package gmail.fopypvp174.cmloja.commands;

import gmail.fopypvp174.cmloja.configurations.LojaConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ItemGenerate implements CommandExecutor {

    private LojaConfig lojaConfig;

    public ItemGenerate(LojaConfig lojaConfig) {
        this.lojaConfig = lojaConfig;
    }

    @Override
    @Deprecated
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Apenas jogadores podem usar esse comando");
        }
        if (args.length == 0) {
            Player p = (Player) sender;
            ItemStack item = p.getItemInHand().clone();
            item.setItemMeta(p.getItemInHand().getItemMeta());
            item.setAmount(1);
            ItemStack compararItem = new ItemStack(item.getType(), 1, item.getData().getData());

            if (item.isSimilar(compararItem)) {
                p.sendMessage(ChatColor.GREEN + "ID do item: (" + ChatColor.WHITE + item.getData().getItemTypeId() + ChatColor.GREEN + ":" + ChatColor.WHITE + item.getData().getData() + ChatColor.GREEN + ").");
                return true;
            }

            if (lojaConfig.equalsItem(item)) {
                p.sendMessage(ChatColor.RED + "Esse item já tem ID: (" + ChatColor.WHITE + lojaConfig.nameItem(item) + ChatColor.RED + ").");
                return true;
            }

            lojaConfig.setItem(item);
            p.sendMessage(ChatColor.GREEN + "ID do item: (" + ChatColor.WHITE + lojaConfig.nameItem(item) + ChatColor.GREEN + ").");
            return true;
        }
        return false;
    }
}
