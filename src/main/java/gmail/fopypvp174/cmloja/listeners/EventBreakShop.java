package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.api.Utilidades;
import gmail.fopypvp174.cmloja.configurations.MessageConfig;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;
import java.util.Set;

public final class EventBreakShop implements Listener {

    private final MessageConfig messageConfig;
    private final Plugin plugin;

    public EventBreakShop(MessageConfig messageConfig, Plugin plugin) {
        this.messageConfig = messageConfig;
        this.plugin = plugin;
    }

    @Deprecated
    @EventHandler
    public void chestBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() != Material.CHEST &&
                e.getBlock().getType() != Material.TRAPPED_CHEST) {
            return;
        }

        Set<BlockFace> directions = EnumSet.of(
                BlockFace.WEST, BlockFace.NORTH,
                BlockFace.EAST, BlockFace.SOUTH);

        directions.stream().map(e.getBlock()::getRelative).forEach(relative -> {
            if (relative.getType() != Material.WALL_SIGN) {
                return;
            }

            Sign sign = (Sign) relative.getState();
            if (!Utilidades.isLojaValid(sign.getLines())) {
                return;
            }

            if (e.getPlayer().getName().equals(sign.getLine(0))) {
                return;
            }

            if (e.getPlayer().hasPermission("loja.quebrarloja")) {
                e.setCancelled(false);
                return;
            }
            e.getPlayer().sendMessage(
                    messageConfig
                            .message("mensagens.break_chest_shop")
                            .replace("%p", sign.getLine(0)));

            e.setCancelled(true);
        });
    }

    @Deprecated
    @EventHandler(priority = EventPriority.HIGHEST)
    public void signBreak(BlockBreakEvent e) {

        if (e.getBlock().getType() != Material.WALL_SIGN) {
            return;
        }

        Sign sign = (Sign) e.getBlock().getState();
        if (!Utilidades.isLojaValid(sign.getLines())) {
            return;
        }

        if (e.getPlayer().getName().equals(sign.getLine(0))) {
            return;
        }

        if (e.getPlayer().hasPermission("loja.quebrarloja")) {
            e.setCancelled(false);
            return;
        }
        e.getPlayer().sendMessage(
                messageConfig
                        .message("mensagens.break_chest_shop")
                        .replace("%p", sign.getLine(0)));

        e.setCancelled(true);
    }
}
