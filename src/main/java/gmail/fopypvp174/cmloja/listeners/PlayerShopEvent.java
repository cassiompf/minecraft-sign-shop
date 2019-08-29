package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.configurations.MessageConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public final class PlayerShopEvent implements Listener {

    private final MessageConfig messageConfig;

    public PlayerShopEvent(MessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPreLogin(PlayerLoginEvent e) {
        if (e.getPlayer().getName().equalsIgnoreCase(messageConfig.message("placa.nomeLoja"))) {
            e.setKickMessage(messageConfig.message("mensagens.kick_erro1"));
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, e.getKickMessage());
        }
    }
}
