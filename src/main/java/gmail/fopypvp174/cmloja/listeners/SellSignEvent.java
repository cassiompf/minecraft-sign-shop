package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.api.Utilidades;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import gmail.fopypvp174.cmloja.exceptions.PlayerEqualsTargetException;
import gmail.fopypvp174.cmloja.exceptions.PlayerUnknowItemException;
import gmail.fopypvp174.cmloja.exceptions.SignUnknowSell;
import gmail.fopypvp174.cmloja.handlers.LojaSellServer;
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

public final class SellSignEvent implements Listener {

    private CmLoja plugin;

    public SellSignEvent(CmLoja plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
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

    private void venderPelaPlaca(Player player, Sign sign, ItemStack item) throws PlayerEqualsTargetException, PlayerUnknowItemException, SignUnknowSell {
        if (sign.getLine(0).equals(player.getDisplayName())) {
            throw new PlayerEqualsTargetException("O jogador '" + player.getName() + "' está tentando vender para ele mesmo.");
        }

        Double priceSaleWithoutDiscount = Utilidades.getPrices(LojaEnum.VENDER, sign);
        if (priceSaleWithoutDiscount == 0.0D) {
            throw new SignUnknowSell("A placa {x=" + sign.getLocation().getX() + ",y=" + sign.getLocation().getY() + ",z=" + sign.getLocation().getZ() + "} não tem opção para vender.");
        }

        Integer amoutItemPlayerHas = Utilidades.quantidadeItemInventory(player.getInventory(), item);
        if (amoutItemPlayerHas == 0) {
            throw new PlayerUnknowItemException("O jogador '" + player.getName() + "' está tentando vender um item que ele não tem no inventário.");
        }

        int qntItemPlaca = Integer.parseInt(Utilidades.replace(sign.getLine(1)));
        priceSaleWithoutDiscount = priceSaleWithoutDiscount * amoutItemPlayerHas / qntItemPlaca;
        Double priceSaleWithDiscount = 0.0D;
        for (int i = 0; i <= 100; i++) {
            if ((player.hasPermission("*")) || (player.isOp())) {
                break;
            }
            if (player.hasPermission("loja.vender." + i)) {
                priceSaleWithDiscount = priceSaleWithoutDiscount + priceSaleWithoutDiscount * i / 100.0D;
                break;
            }
        }
        if (priceSaleWithDiscount > 0.0D) {
            String moneyFormatted = String.format("%.2f", priceSaleWithDiscount - priceSaleWithoutDiscount);
            player.sendMessage(this.plugin.getMessageConfig().message("mensagens.vender_vip_vantagem", moneyFormatted));

            moneyFormatted = String.format("%.2f", priceSaleWithDiscount);
            player.sendMessage(this.plugin.getMessageConfig().message("mensagens.vender_success_sign", amoutItemPlayerHas, moneyFormatted));

            this.plugin.getEcon().depositPlayer(player, priceSaleWithDiscount);

            LojaSellServer eventBuy = new LojaSellServer(player, priceSaleWithDiscount, item, amoutItemPlayerHas);
            Bukkit.getServer().getPluginManager().callEvent(eventBuy);
        } else {
            String dinheiroFormatado = String.format("%.2f", priceSaleWithoutDiscount);
            player.sendMessage(this.plugin.getMessageConfig().message("mensagens.vender_success_sign", amoutItemPlayerHas, dinheiroFormatado));

            this.plugin.getEcon().depositPlayer(player, priceSaleWithoutDiscount);

            LojaSellServer eventBuy = new LojaSellServer(player, priceSaleWithoutDiscount, item, amoutItemPlayerHas);
            Bukkit.getServer().getPluginManager().callEvent(eventBuy);
        }
        item.setAmount(amoutItemPlayerHas);
        player.getInventory().removeItem(item);
    }
}
