package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.api.Utilidades;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import gmail.fopypvp174.cmloja.events.LojaSellOtherPlayer;
import gmail.fopypvp174.cmloja.exceptions.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EventoVenderChest implements Listener {

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

        if (sign.getLine(0).equals(plugin.getMessageConfig().message("placa.nomeLoja"))) {
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
        if ((!block.getType().equals(Material.CHEST)) && (!block.getType().equals(Material.TRAPPED_CHEST))) {
            return;
        }

        Chest chest = (Chest) block.getState();
        ItemStack item = Utilidades.getItemLoja(sign.getLines());

        try {
            venderPelaPlaca(player, sign, chest, item);
        } catch (PlayerEqualsTargetException error) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro1"));
        } catch (PlayerUnknowItemException error) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro3"));
        } catch (TargetUnknowException error) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.player_unknown").replace("%p", sign.getLine(0)));
        } catch (SignUnknowSell error) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro5"));
        } catch (InventoryFullException error) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro4"));
        } catch (TargetMoneyException error) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro2").replace("%p", sign.getLine(0)));
        }
    }

    public void venderPelaPlaca(Player player, Sign placa, Chest chest, ItemStack item) throws PlayerEqualsTargetException, PlayerUnknowItemException, TargetUnknowException, SignUnknowSell, InventoryFullException, TargetMoneyException {
        if (placa.getLine(0).equals(player.getDisplayName())) {
            throw new PlayerEqualsTargetException("O jogador '" + player.getName() + "' está tentando vender para ele mesmo.");
        }

        Integer qntItemJogadorTem = Utilidades.quantidadeItemInventory(player.getInventory(), item);
        if (qntItemJogadorTem == 0) {
            throw new PlayerUnknowItemException("O jogador '" + player.getName() + "' está tentando vender um item que ele não tem no inventário.");
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(placa.getLine(0));
        if (target == null) {
            throw new TargetUnknowException("Jogador com o nick '" + placa.getLine(0) + "' não foi encontrado.");
        }

        Double valorVenda = (double) Utilidades.getPrices(LojaEnum.VENDER, placa);
        if (valorVenda == 0) {
            throw new SignUnknowSell("A placa {x=" + placa.getLocation().getX() + ",y=" + placa.getLocation().getY() + ",z=" + placa.getLocation().getZ() + "} não tem opção para vender.");
        }

        if (!Utilidades.temEspacoInvParaItem(chest.getInventory(), item, qntItemJogadorTem)) {
            throw new InventoryFullException("O baú {x=" + chest.getLocation().getX() + ",y= + " + chest.getLocation().getY() + ",z=" + chest.getLocation().getZ() + "} não tem espaço para receber itens de venda do jogador ." + player.getName());
        }

        Integer qntItemPlaca = Integer.parseInt(Utilidades.replace(placa.getLine(1)));
        Double valorFinalVenda = (qntItemJogadorTem / qntItemPlaca) * valorVenda;

        if (plugin.getEcon().getBalance(target) < valorFinalVenda) {
            throw new TargetMoneyException("O jogador " + target.getName() + " não tem dinheiro para pagar o jogador " + player.getName() + " pela venda.");
        }

        String dinheiroFormatado = String.format("%.2f", valorFinalVenda);
        player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_success_chest", qntItemPlaca, dinheiroFormatado, target));

        item.setAmount(qntItemJogadorTem);

        player.getInventory().removeItem(item);
        chest.getInventory().addItem(item);

        plugin.getEcon().withdrawPlayer(target, valorFinalVenda);
        plugin.getEcon().depositPlayer(player, valorFinalVenda);

        LojaSellOtherPlayer eventBuy = new LojaSellOtherPlayer(target, player, valorFinalVenda, item, qntItemPlaca);
        Bukkit.getServer().getPluginManager().callEvent(eventBuy);
    }
}
