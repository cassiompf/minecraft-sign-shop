package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class EventoCriar implements Listener {

    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCriar(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (plugin.getUtilidades().isLoja(e.getLines())) {
            e.setLine(2, plugin.getUtilidades().updatePriceSign(e.getLines()));
            if (p.hasPermission("loja.admin")) {
                if (!plugin.getUtilidades().checkBau(e.getBlock())) {
                    if (!e.getLine(0).equals(plugin.getMessageConfig().message("placa.nomeLoja", 0, null, null))) {
                        e.getBlock().breakNaturally();
                        p.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro1", 0, null, null));
                        return;
                    }
                }
                p.sendMessage(plugin.getMessageConfig().message("mensagens.criar_success", 0, null, null));
            } else if (p.hasPermission("loja.jogador")) {
                if (plugin.getUtilidades().checkBau(e.getBlock())) {
                    if (e.getLine(0).equals(p.getName())) {
                        if (!plugin.getUtilidades().isItemValid(e.getLine(3))) {
                            e.getBlock().breakNaturally();
                            p.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro2", 0, null, null));
                            return;
                        }
                    } else {
                        e.getBlock().breakNaturally();
                        p.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro3", 0, null, null));
                        return;
                    }
                } else {
                    e.getBlock().breakNaturally();
                    p.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro4", 0, null, null));
                    return;
                }
                p.sendMessage(plugin.getMessageConfig().message("mensagens.criar_success", 0, null, null));
            } else {
                e.getBlock().breakNaturally();
                p.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro5", 0, null, null));
                return;
            }
        }
    }
}
