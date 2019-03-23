package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.CmLoja;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public final class EventoPlayer implements Listener {

    private CmLoja plugin;

    public EventoPlayer(CmLoja plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onPreLogin(PlayerLoginEvent e) {
        if (e.getPlayer().getName().equalsIgnoreCase(plugin.getMessageConfig().message("placa.nomeLoja"))) {
            e.setKickMessage(plugin.getMessageConfig().message("mensagens.kick_erro1"));
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, e.getKickMessage());
            return;
        }
    }
}
