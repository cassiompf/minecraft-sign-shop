package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventoPlayer implements Listener {

    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPreLogin(PlayerLoginEvent e) {
        if (e.getPlayer().getName().equalsIgnoreCase(plugin.getMessageConfig().message("placa.nomeLoja", 0, null, null))) {
            e.setKickMessage(plugin.getMessageConfig().message("mensagens.kick_erro1", 0, null, null));
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, e.getKickMessage());
            return;
        }
    }
}
