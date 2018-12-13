package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;

public class EventoCriar implements Listener {

    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler(ignoreCancelled = true)
    public void onCriar(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (plugin.getUtilidades().isLoja(e.getLines())) {
            String[] CeV = plugin.getUtilidades().replace(e.getLine(2)).replace("v", "").replace("V", "").replace("c", "").replace("C", "").split(":");
            if (CeV[0].matches("^(?i)(0)+(\\s|$)")) {
                e.setLine(2, "§4V§r " + CeV[1]);
            } else if (CeV[1].matches("^(?i)(0)+(\\s|$)")) {
                e.setLine(2, "§2C§r " + CeV[0]);
            } else {
                e.setLine(2, "§2C§r " + CeV[0] + " : " + "§4V§r " + CeV[1]);
            }
            if (p.hasPermission("loja.admin")) {
                if (!plugin.getUtilidades().checarBau(e.getBlock())) {
                    if (!e.getLine(0).equals("[Loja]")) {
                        e.getBlock().breakNaturally();
                        p.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro1", 0, null, null));
                        return;
                    }
                }
                p.sendMessage(plugin.getMessageConfig().message("mensagens.criar_success", 0, null, null));
            } else if (p.hasPermission("loja.jogador")) {
                if (plugin.getUtilidades().checarBau(e.getBlock())) {
                    if (e.getLine(0).equals(p.getName())) {
                        if (!plugin.getUtilidades().itemValido(e.getLine(3))) {
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
