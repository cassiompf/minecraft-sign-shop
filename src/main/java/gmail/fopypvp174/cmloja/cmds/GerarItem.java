package gmail.fopypvp174.cmloja.cmds;

import gmail.fopypvp174.cmloja.CmLoja;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GerarItem implements CommandExecutor {

    private CmLoja plugin = CmLoja.getPlugin(CmLoja.class);

    @Override
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

            if (plugin.getLoja().equalsItem(item)) {
                p.sendMessage(ChatColor.RED + "Esse item já tem ID: (" + ChatColor.WHITE + plugin.getLoja().nameItem(item) + ChatColor.RED + ").");
                return true;
            }

            plugin.getLoja().setItem(item);
            p.sendMessage(ChatColor.GREEN + "ID do item: (" + ChatColor.WHITE + plugin.getLoja().nameItem(item) + ChatColor.GREEN + ").");
            return true;
        }
        return false;
    }
}
