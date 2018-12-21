package gmail.fopypvp174.cmloja.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class LojaBuyServer extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Double money;
    private ItemStack itemBuy;
    private Integer itensAmount;

    public LojaBuyServer(Player player, Double money, ItemStack itemBuy, Integer itensAmount) {
        this.player = player;
        this.money = money;
        this.itemBuy = itemBuy;
        this.itensAmount = itensAmount;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Double getMoney() {
        return money;
    }

    public ItemStack getItemBuy() {
        return itemBuy;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Integer getItensAmount() {
        return itensAmount;
    }

}
