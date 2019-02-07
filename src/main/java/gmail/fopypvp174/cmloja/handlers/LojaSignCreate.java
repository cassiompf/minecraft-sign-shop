package gmail.fopypvp174.cmloja.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;


public final class LojaSignCreate extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String[] signLines;
    private ItemStack item;

    public LojaSignCreate(Player player, String[] signLines, ItemStack item) {
        this.player = player;
        this.signLines = signLines;
        this.item = item;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public String[] getSignLines() {
        return signLines;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
