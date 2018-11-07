package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.Main;
import gmail.fopypvp174.cmloja.api.Utilidades;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;

public final class EventoCriar implements Listener {

    @EventHandler
    public void onCriar(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (Utilidades.isLoja(e.getLines())) {
            String[] CeV = Utilidades.replace(e.getLine(2)).replace("v", "").replace("V", "").replace("c", "").replace("C", "").split(":");
            e.setLine(2, "§2C§r " + CeV[0] + " : " + "§4V§r " + CeV[1]);
            if (p.hasPermission("loja.admin")) {
                if (!checarBau(e.getBlock())) {
                    if (!e.getLine(0).equals("[Loja]")) {
                        e.getBlock().breakNaturally();
                        p.sendMessage(Main.messageConfig.message("mensagens.criar_erro1", 0, null, null));
                        return;
                    }
                }
                p.sendMessage(Main.messageConfig.message("mensagens.criar_success", 0, null, null));
            } else if (p.hasPermission("loja.jogador")) {
                if (checarBau(e.getBlock())) {
                    if (e.getLine(0).equals(p.getName())) {
                        if (!itemValido(e.getLine(3))) {
                            e.getBlock().breakNaturally();
                            p.sendMessage(Main.messageConfig.message("mensagens.criar_erro2", 0, null, null));
                            return;
                        }
                    } else {
                        e.getBlock().breakNaturally();
                        p.sendMessage(Main.messageConfig.message("mensagens.criar_erro3", 0, null, null));
                        return;
                    }
                } else {
                    e.getBlock().breakNaturally();
                    p.sendMessage(Main.messageConfig.message("mensagens.criar_erro4", 0, null, null));
                    return;
                }
                p.sendMessage(Main.messageConfig.message("mensagens.criar_success", 0, null, null));
            } else {
                e.getBlock().breakNaturally();
                p.sendMessage(Main.messageConfig.message("mensagens.criar_erro5", 0, null, null));
                return;
            }
        }
    }

    public boolean itemValido(String valor) {
        if (valor.matches("(\\d)+(\\#(\\w){4,4}){1}(\\s|$)")) {
            System.out.println(valor);
            if (!Main.loja.isItem(valor)) {
                return false;
            }
        } else if (valor.matches("(\\d)+(\\:(\\d){1,2}){1}(\\s|$)")) {
            String[] item = valor.split(":");
            if (Short.parseShort(item[1]) > 15) {
                return false;
            }
        }
        return true;
    }

    public boolean checarBau(Block b) {
        Block bau = b.getRelative(((Sign) b.getState().getData()).getAttachedFace());
        if ((b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN))
                && (bau.getType().equals(Material.CHEST) || bau.getType().equals(Material.TRAPPED_CHEST))) {
            return true;
        }
        return false;
    }

    public String updatePlaca(int position, String linha) {
        String valor = null;
        if (position == 2) {
            String[] CeV = Utilidades.replace(linha).replace("v", "").replace("V", "").replace("c", "").replace("C", "").split(":");
            valor = "§2C§r " + CeV[0] + " : " + "§4V§r " + CeV[1];
        }
        return valor;
    }


}
