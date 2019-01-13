package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.api.Utilidades;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import gmail.fopypvp174.cmloja.events.LojaSellServer;
import gmail.fopypvp174.cmloja.exceptions.PlayerEqualsTargetException;
import gmail.fopypvp174.cmloja.exceptions.PlayerUnknowItemException;
import gmail.fopypvp174.cmloja.exceptions.SignUnknowSell;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EventoVenderSign implements Listener {

    private CmLoja plugin = CmLoja.getPlugin(CmLoja.class);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onComprar(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (e.getClickedBlock().getType() != Material.SIGN_POST &&
                e.getClickedBlock().getType() != Material.WALL_SIGN) {
            return;
        }

        Sign sign = (Sign) e.getClickedBlock().getState();
        if (!Utilidades.isLojaValid(sign.getLines())) {
            return;
        }

        if (!sign.getLine(0).equals(plugin.getMessageConfig().message("placa.nomeLoja"))) {
            return;
        }

        Player player = e.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            if (player.isOp() || player.hasPermission("loja.admin")) {
                sign.getBlock().breakNaturally(null);
                e.setCancelled(true);
                return;
            }
        }

        Block block = e.getClickedBlock().getRelative(((org.bukkit.material.Sign) sign.getData()).getAttachedFace());
        if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)) {
            return;
        }

        ItemStack item = Utilidades.getItemLoja(sign.getLines());

        try {
            venderPelaPlaca(player, sign, item);
        } catch (PlayerEqualsTargetException error1) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro1"));
        } catch (PlayerUnknowItemException error2) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro3"));
        } catch (SignUnknowSell error3) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro5"));
        }
    }

    public void venderPelaPlaca(Player player, Sign placa, ItemStack item) throws PlayerEqualsTargetException, PlayerUnknowItemException, SignUnknowSell {
        if (placa.getLine(0).equals(player.getDisplayName())) {
            throw new PlayerEqualsTargetException("O jogador '" + player.getName() + "' está tentando vender para ele mesmo.");
        }

        Integer qntItemJogadorTem = Utilidades.quantidadeItemInventory(player.getInventory(), item);
        if (qntItemJogadorTem == 0) {
            throw new PlayerUnknowItemException("O jogador '" + player.getName() + "' está tentando vender um item que ele não tem no inventário.");
        }

        Double valorVenda = (double) Utilidades.getPrices(LojaEnum.VENDER, placa);
        if (valorVenda == 0) {
            throw new SignUnknowSell("A placa {x=" + placa.getLocation().getX() + ",y=" + placa.getLocation().getY() + ",z=" + placa.getLocation().getZ() + "} não tem opção para vender.");
        }

        Integer qntItemPlaca = Integer.parseInt(Utilidades.replace(placa.getLine(1)));
        boolean descontoVip = false;
        Double valorFinalVenda = valorVenda;

        for (int i = 0; i <= 100; i++) {
            if (player.hasPermission("*") || player.isOp()) {
                break;
            }
            if (player.hasPermission("loja.vender." + i)) {
                valorFinalVenda = valorVenda + ((valorVenda * i) / 100);
                descontoVip = true;
                break;
            }
        }

        String dinheiroFormatado = String.format("%.2f", (valorFinalVenda - valorVenda));
        if (descontoVip) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_vip_vantagem", dinheiroFormatado));
        }

        valorFinalVenda *= (qntItemJogadorTem / qntItemPlaca);
        dinheiroFormatado = String.format("%.2f", valorFinalVenda);
        player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_success_sign", qntItemJogadorTem, dinheiroFormatado));

        item.setAmount(qntItemJogadorTem);
        player.getInventory().removeItem(item);

        plugin.getEcon().depositPlayer(player, valorVenda);

        LojaSellServer eventBuy = new LojaSellServer(player, valorFinalVenda, item, qntItemJogadorTem);
        Bukkit.getServer().getPluginManager().callEvent(eventBuy);
    }
}
