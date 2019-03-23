package gmail.fopypvp174.cmloja.handlers;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public final class LojaBuyOtherPlayer extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Double money;
    private ItemStack itemBuy;
    private OfflinePlayer ownerLoja;
    private Integer itensAmount;

    public LojaBuyOtherPlayer(OfflinePlayer ownerLoja, Player player, Double money, ItemStack itemBuy, Integer itensAmount) {
        this.ownerLoja = ownerLoja;
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

    public ItemStack getItemBuy() {
        return itemBuy;
    }

    public Double getMoney() {
        return money;
    }

    public Integer getItensAmount() {
        return itensAmount;
    }

    public OfflinePlayer getOwnerLoja() {
        return ownerLoja;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
