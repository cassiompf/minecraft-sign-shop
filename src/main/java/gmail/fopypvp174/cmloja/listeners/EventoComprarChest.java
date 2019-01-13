package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.api.Utilidades;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import gmail.fopypvp174.cmloja.events.LojaBuyOtherPlayer;
import gmail.fopypvp174.cmloja.exceptions.*;
import org.bukkit.Bukkit;
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

public class EventoComprarChest implements Listener {
    private CmLoja plugin = CmLoja.getPlugin(CmLoja.class);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onComprar(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
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

        Block block = e.getClickedBlock().getRelative(((org.bukkit.material.Sign) sign.getData()).getAttachedFace());
        if ((!block.getType().equals(Material.CHEST)) && (!block.getType().equals(Material.TRAPPED_CHEST))) {
            return;
        }


        Player player = e.getPlayer();
        ItemStack item = Utilidades.getItemLoja(sign.getLines());
        Chest chest = (Chest) block.getState();
        try {
            comprarPeloBau(player, sign, chest, item);
        } catch (PlayerEqualsTargetException error1) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.comprar_erro3"));
        } catch (PlayerMoneyException error2) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.comprar_erro1"));
        } catch (EmptyChestException error3) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.comprar_erro2"));
        } catch (InventoryFullException erro4) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.inventory_full"));
        } catch (TargetUnknowException erro5) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.player_unknown").replace("%p", sign.getLine(0)));
        }
    }

    public void comprarPeloBau(Player player, Sign placa, Chest chest, ItemStack item) throws EmptyChestException, InventoryFullException, TargetUnknowException, PlayerMoneyException, PlayerEqualsTargetException {
        if (placa.getLine(0).equals(player.getDisplayName())) {
            throw new PlayerEqualsTargetException("O jogador '" + player.getName() + "' está tentando comprar dele mesmo.");
        }

        Double valorCompra = (double) Utilidades.getPrices(LojaEnum.COMPRAR, placa);

        if (plugin.getEcon().getBalance(player) < valorCompra) {
            throw new PlayerMoneyException("O jogador '" + player.getName() + "' não tem dinheiro suficiente para fazer a compra.");
        }

        int quantiaPlaca = Short.parseShort(Utilidades.replace(placa.getLine(1)));
        if (!chest.getInventory().contains(item, quantiaPlaca)) {
            throw new EmptyChestException("Não tem item suficiente no baú para fazer a compra.");
        }

        if (!Utilidades.temEspacoInvParaItem(player.getInventory(), item, quantiaPlaca)) {
            throw new InventoryFullException("Inventário do jogador está lotado e não tem como receber os itens.");
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(placa.getLine(0));
        if (target == null) {
            throw new TargetUnknowException("Jogador com o nick '" + placa.getLine(0) + "' não foi encontrado.");
        }

        plugin.getEcon().depositPlayer(target, valorCompra);
        plugin.getEcon().withdrawPlayer(player, valorCompra);

        item.setAmount(quantiaPlaca);

        player.getInventory().addItem(item);
        removeItemBau(chest, item, quantiaPlaca);

        String dinheiroFormatado = String.format("%.2f", valorCompra);
        player.sendMessage(plugin.getMessageConfig().message("mensagens.comprar_success_chest", quantiaPlaca, dinheiroFormatado, target));

        LojaBuyOtherPlayer eventBuy = new LojaBuyOtherPlayer(target, player, valorCompra, item, quantiaPlaca);
        Bukkit.getServer().getPluginManager().callEvent(eventBuy);
    }

    public void removeItemBau(Chest chest, ItemStack itemStack, int quantidade) {
        int amout = quantidade;
        for (int i = 0; i < chest.getInventory().getSize(); i++) {
            ItemStack item = chest.getInventory().getItem(i);
            if (item.isSimilar(itemStack)) {
                if ((amout - item.getAmount()) > 0) {
                    amout -= item.getAmount();
                    chest.getInventory().setItem(i, new ItemStack(Material.AIR));
                } else if ((amout - item.getAmount()) == 0) {
                    chest.getInventory().setItem(i, new ItemStack(Material.AIR));
                    break;
                } else {
                    item.setAmount(item.getAmount() - amout);
                    break;
                }
            }
        }
    }
}
