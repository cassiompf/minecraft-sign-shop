package gmail.fopypvp174.cmloja.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class LojaSellOtherPlayer extends Event {

    private static final HandlerList handlers = new HandlerList();
    private OfflinePlayer ownerLoja;
    private Player player;
    private Double money;
    private ItemStack itemSell;
    private Integer itensAmount;

    public LojaSellOtherPlayer(OfflinePlayer ownerLoja, Player player, Double money, ItemStack itemSell, Integer itensAmount) {
        this.ownerLoja = ownerLoja;
        this.player = player;
        this.money = money;
        this.itemSell = itemSell;
        this.itensAmount = itensAmount;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public OfflinePlayer getOwnerLoja() {
        return ownerLoja;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getItensAmount() {
        return itensAmount;
    }

    public ItemStack getItemSell() {
        return itemSell;
    }

    public Double getMoney() {
        return money;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
