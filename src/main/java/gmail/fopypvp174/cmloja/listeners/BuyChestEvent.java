package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.api.Utilidades;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import gmail.fopypvp174.cmloja.exceptions.*;
import gmail.fopypvp174.cmloja.handlers.LojaBuyOtherPlayer;
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

public final class BuyChestEvent implements Listener {
    private CmLoja plugin;

    public BuyChestEvent(CmLoja plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    @Deprecated
    private void onComprar(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (e.getClickedBlock().getType() != Material.WALL_SIGN) {
            return;
        }

        Sign sign = (Sign) e.getClickedBlock().getState();
        if (!Utilidades.isLojaValid(sign.getLines())) {
            return;
        }
        String placaLoja = plugin.getMessageConfig().getCustomConfig().getString("placa.nomeLoja");

        if (Utilidades.replaceShopName(sign.getLine(0)).equals(placaLoja)) {
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
            player.sendMessage(plugin.getMessageConfig().message("mensagens.player_unknown").replace("%p", Utilidades.replaceShopName(sign.getLine(0))));
        } catch (SignUnknowBuy erro6) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.comprar_erro4"));
        }
    }

    @Deprecated
    public void comprarPeloBau(Player player, org.bukkit.block.Sign sign, Chest chest, ItemStack item)
            throws EmptyChestException, InventoryFullException, TargetUnknowException, PlayerMoneyException, PlayerEqualsTargetException, SignUnknowBuy {
        String line1 = Utilidades.replaceShopName(sign.getLine(0));
        if (line1.equals(player.getDisplayName())) {
            throw new PlayerEqualsTargetException("O jogador '" + player.getName() + "' está tentando comprar dele mesmo.");
        }
        Double priceBuy = Utilidades.getPrices(LojaEnum.COMPRAR, sign);
        if (priceBuy == 0.0D) {
            throw new SignUnknowBuy("A placa {x=" + sign.getLocation().getX() + ",y=" + sign.getLocation().getY() + ",z=" + sign.getLocation().getZ() + "} não tem opção para comprar.");
        }
        if (this.plugin.getEcon().getBalance(player) < priceBuy) {
            throw new PlayerMoneyException("O jogador '" + player.getName() + "' não tem dinheiro suficiente para fazer a compra.");
        }
        int amountSign = Short.parseShort(Utilidades.replace(sign.getLine(1)));
        int amountChest = Utilidades.quantidadeItemInventory(chest.getInventory(), item);
        if (amountChest < amountSign) {
            throw new EmptyChestException("Não tem item suficiente no baú para fazer a compra.");
        }
        if (!Utilidades.haveSlotClearInv(player.getInventory(), item, amountSign)) {
            throw new InventoryFullException("Inventário do jogador está lotado e não tem como receber os itens.");
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(line1);
        if (target == null) {
            throw new TargetUnknowException("Jogador com o nick '" + Utilidades.replaceShopName(sign.getLine(0)) + "' não foi encontrado.");
        }
        this.plugin.getEcon().depositPlayer(target, priceBuy);
        this.plugin.getEcon().withdrawPlayer(player, priceBuy);

        item.setAmount(amountSign);

        player.getInventory().addItem(item);
        removeItemBau(chest, item, amountSign);

        String moneyFormatted = String.format("%.2f", priceBuy);
        player.sendMessage(this.plugin.getMessageConfig().message("mensagens.comprar_success_chest", amountSign, moneyFormatted, target));

        LojaBuyOtherPlayer eventBuy = new LojaBuyOtherPlayer(target, player, priceBuy, item, amountSign);
        Bukkit.getServer().getPluginManager().callEvent(eventBuy);
    }

    private void removeItemBau(Chest chest, ItemStack itemStack, int amount) {
        int var = amount;
        for (int i = 0; i < chest.getInventory().getSize(); i++) {
            ItemStack item = chest.getInventory().getItem(i);
            if (item != null) {
                if (item.isSimilar(itemStack)) {
                    if (var - item.getAmount() > 0) {
                        var -= item.getAmount();
                        chest.getInventory().setItem(i, new ItemStack(Material.AIR));
                    } else {
                        if (var - item.getAmount() == 0) {
                            chest.getInventory().setItem(i, new ItemStack(Material.AIR));
                            break;
                        }
                        if (var - item.getAmount() < 0) {
                            item.setAmount(item.getAmount() - var);
                            break;
                        }
                    }
                }
            }
        }
    }
}
