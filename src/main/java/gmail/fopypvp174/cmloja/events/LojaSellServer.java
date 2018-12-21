package gmail.fopypvp174.cmloja.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class LojaSellServer extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Double money;
    private ItemStack itemSell;
    private Integer itensAmount;

    public LojaSellServer(Player player, Double money, ItemStack itemSell, Integer itensAmount) {
        this.player = player;
        this.money = money;
        this.itemSell = itemSell;
        this.itensAmount = itensAmount;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemSell() {
        return itemSell;
    }

    public Integer getItensAmount() {
        return itensAmount;
    }

    public Double getMoney() {
        return money;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
