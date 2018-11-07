package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.Main;
import gmail.fopypvp174.cmloja.api.Utilidades;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class EventoVender implements Listener {

    @EventHandler
    public void onVender(final PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (e.getClickedBlock().getType().equals(Material.WALL_SIGN) || e.getClickedBlock().getType().equals(Material.SIGN_POST)) {
            final Sign sign = (Sign) e.getClickedBlock().getState();
            final Player p = e.getPlayer();
            if (Utilidades.isLoja(sign.getLines())) {
                if (sign.getLine(0).equals(p.getDisplayName())) {
                    p.sendMessage(Main.messageConfig.message("mensagens.vender_erro1", 0, null, null));
                    return;
                }
                final ItemStack item = Utilidades.itemLoja(sign.getLines());
                final Block block = e.getClickedBlock().getRelative(((org.bukkit.material.Sign) sign.getData()).getAttachedFace());

                final OfflinePlayer target = Bukkit.getOfflinePlayer(sign.getLine(0));
                if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)) {
                    final Chest chest = (Chest) block.getState();
                    if (target != null) {
                        sellItens(p, target, chest, item, sign);
                    } else {
                        p.sendMessage(Main.messageConfig.message("mensagens.player_unknown", 0, null, target));
                        return;
                    }
                } else {
                    sellItens(p, null, null, item, sign);
                }
            }
        }
    }

    public final void sellItens(Player p, OfflinePlayer target, Chest chest, ItemStack item, Sign sign) {
        int qntItensJogadorPodeVender = 0;
        int qntItensBauSuportaVender = 0;

        double qntPlaca = Double.parseDouble(sign.getLine(1));
        double valor1Item = Utilidades.valores(LojaEnum.VENDER, sign) * (1 / qntPlaca);
        double valorFinalVenda = 0;
        List<Short> position = new ArrayList<>();
        Inventory invPlayer = p.getInventory();
        for (short i = 0; i < invPlayer.getSize(); i++) {
            ItemStack itemInv = invPlayer.getItem(i);
            if (itemInv != null && itemInv.getAmount() != 0) {
                if (item.isSimilar(itemInv)) {
                    position.add(i);
                    qntItensJogadorPodeVender += itemInv.getAmount();
                }
            }
        }
        ItemStack itemStack = item.clone();
        if (chest != null) {
            double moneyP = Main.econ.getBalance(target);
            double valorDeX = Math.ceil(moneyP / valor1Item);
            Inventory invBau = chest.getInventory();
            //Percorrer o baú e salvar quantos itens ele suporta vender.
            for (short i = 0; i < invBau.getSize(); i++) {
                ItemStack itemInv = invBau.getItem(i);
                if (itemInv != null && itemInv.getAmount() != 0) {
                    if (item.isSimilar(itemInv)) {
                        qntItensBauSuportaVender += 64 - itemInv.getAmount();
                    }
                } else {
                    qntItensBauSuportaVender += 64;
                }
            }

            if (valorDeX != 0) {
                if (qntItensJogadorPodeVender != 0) {
                    if (qntItensBauSuportaVender != 0) {
                        if (valorDeX > qntItensJogadorPodeVender) {
                            if (qntItensJogadorPodeVender > qntItensBauSuportaVender) {
                                itemStack.setAmount(qntItensBauSuportaVender);
                                valorFinalVenda = qntItensBauSuportaVender * valor1Item;
                            } else {
                                itemStack.setAmount(qntItensJogadorPodeVender);
                                valorFinalVenda = qntItensJogadorPodeVender * valor1Item;
                            }
                        } else {
                            if (valorDeX > qntItensBauSuportaVender) {
                                itemStack.setAmount(qntItensBauSuportaVender);
                                valorFinalVenda = qntItensBauSuportaVender * valor1Item;
                            } else {
                                itemStack.setAmount((int) valorDeX);
                                valorFinalVenda = valorDeX * valor1Item;
                            }
                        }
                        invBau.addItem(itemStack);
                        int qntRemover = itemStack.getAmount();
                        for (int i = 0; i < position.size(); i++) {
                            ItemStack itemStack2 = invPlayer.getItem(position.get(i));
                            if (itemStack2.getAmount() > qntRemover) {
                                itemStack2.setAmount(itemStack2.getAmount() - qntRemover);
                                break;
                            }
                            if (itemStack2.getAmount() == qntRemover) {
                                invPlayer.clear(position.get(i));
                                break;
                            }
                            if (itemStack2.getAmount() < qntRemover) {
                                qntRemover -= itemStack2.getAmount();
                                invPlayer.clear(position.get(i));
                            }
                            if (qntRemover == 0) {
                                break;
                            }
                        }
                        Utilidades.removeMoneyVault(p, valorFinalVenda);
                        Utilidades.removeMoneyVault(target, valorFinalVenda);
                        p.sendMessage(Main.messageConfig.message("mensagens.vender_success", itemStack.getAmount(), String.format("%.2f", valorFinalVenda), null));

                    } else {
                        p.sendMessage(Main.messageConfig.message("mensagens.vender_erro4", 0, null, null));
                    }
                } else {
                    p.sendMessage(Main.messageConfig.message("mensagens.vender_erro3", 0, null, null));
                }
            } else {
                p.sendMessage(Main.messageConfig.message("mensagens.vender_erro2", 0, null, target));
            }
        } else {
            if (qntItensJogadorPodeVender != 0) {
                valorFinalVenda = qntItensJogadorPodeVender * valor1Item;
                for (int i = 0; i < position.size(); i++) {
                    invPlayer.clear(position.get(i));
                }
                p.sendMessage(Main.messageConfig.message("mensagens.vender_success", qntItensJogadorPodeVender, String.format("%.2f", valorFinalVenda), null));
                for (int i = 0; i <= 100; i++) {
                    if (p.hasPermission("*") || p.isOp()) {
                        break;
                    }
                    if (p.hasPermission("loja.vender." + i)) {
                        valorFinalVenda += valorFinalVenda * (i / 100);
                        p.sendMessage(Main.messageConfig.message("mensagens.vender_vip_vantagem", i, null, null));
                        break;
                    }
                }
                Utilidades.darMoneyVault(p, valorFinalVenda);
            } else {
                p.sendMessage(Main.messageConfig.message("mensagens.vender_erro3", 0, null, null));
            }
        }
    }
}
