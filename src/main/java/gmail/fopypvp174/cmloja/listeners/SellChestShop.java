package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.api.Utilidades;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import gmail.fopypvp174.cmloja.exceptions.*;
import gmail.fopypvp174.cmloja.handlers.LojaSellOtherPlayer;
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

public final class SellChestShop implements Listener {

    private CmLoja plugin;

    public SellChestShop(CmLoja plugin) {
        this.plugin = plugin;
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    @Deprecated
    private void onComprar(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
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
            player.sendMessage(plugin.getMessageConfig().message("mensagens.player_unknown").replace("%p", Utilidades.replaceShopName(sign.getLine(0))));
        } catch (SignUnknowSell error) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro5"));
        } catch (InventoryFullException error) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro4"));
        } catch (TargetMoneyException error) {
            player.sendMessage(plugin.getMessageConfig().message("mensagens.vender_erro2").replace("%p", Utilidades.replaceShopName(sign.getLine(0))));
        }
    }

    @Deprecated
    public void venderPelaPlaca(Player player, org.bukkit.block.Sign sign, Chest chest, ItemStack item)
            throws PlayerEqualsTargetException, PlayerUnknowItemException, TargetUnknowException, SignUnknowSell, InventoryFullException, TargetMoneyException {
        String line1 = Utilidades.replaceShopName(sign.getLine(0));
        if (line1.equals(player.getDisplayName())) {
            throw new PlayerEqualsTargetException("O jogador '" + player.getName() + "' está tentando vender para ele mesmo.");
        }
        Double priceSell = Utilidades.getPrices(LojaEnum.VENDER, sign);
        if (priceSell == 0.0D) {
            throw new SignUnknowSell("A placa {x=" + sign.getLocation().getX() + ",y=" + sign.getLocation().getY() + ",z=" + sign.getLocation().getZ() + "} não tem opção para vender.");
        }
        Integer amoutItemPlayerHas = Utilidades.quantidadeItemInventory(player.getInventory(), item);
        if (amoutItemPlayerHas == 0) {
            throw new PlayerUnknowItemException("O jogador '" + player.getName() + "' está tentando vender um item que ele não tem no inventário.");
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(line1);
        if (target == null) {
            throw new TargetUnknowException("Jogador com o nick '" + line1 + "' não foi encontrado.");
        }
        if (!Utilidades.haveSlotClearInv(chest.getInventory(), item, amoutItemPlayerHas)) {
            throw new InventoryFullException("O baú {x=" + chest.getLocation().getX() + ",y= + " + chest.getLocation().getY() + ",z=" + chest.getLocation().getZ() + "} não tem espaço para receber itens de venda do jogador ." + player.getName());
        }
        Integer amoutItemSign = Integer.parseInt(Utilidades.replace(sign.getLine(1)));
        double finalValueSale = (amoutItemPlayerHas / amoutItemSign) * priceSell;
        if (this.plugin.getEcon().getBalance(target) < finalValueSale) {
            throw new TargetMoneyException("O jogador " + target.getName() + " não tem dinheiro para pagar o jogador " + player.getName() + " pela venda.");
        }

        String moneyFormatted = String.format("%.2f", finalValueSale);
        player.sendMessage(this.plugin.getMessageConfig().message("mensagens.vender_success_chest", amoutItemPlayerHas, moneyFormatted, target));

        item.setAmount(amoutItemPlayerHas);
        player.getInventory().removeItem(item);

        if (item.getMaxStackSize() != 1) {
            chest.getInventory().addItem(item);
        } else {
            item.setAmount(1);
            for (int a = 0; a < amoutItemPlayerHas; a++) {
                for (int b = 0; b < chest.getInventory().getSize(); b++) {
                    if (chest.getInventory().getItem(b) == null) {
                        chest.getInventory().setItem(b, item);
                        break;
                    }
                }
            }

        }

        this.plugin.getEcon().withdrawPlayer(target, finalValueSale);
        this.plugin.getEcon().depositPlayer(player, finalValueSale);

        LojaSellOtherPlayer eventBuy = new LojaSellOtherPlayer(target, player, finalValueSale, item, amoutItemPlayerHas);
        Bukkit.getServer().getPluginManager().callEvent(eventBuy);
    }
}
