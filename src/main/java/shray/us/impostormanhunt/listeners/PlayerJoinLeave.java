package shray.us.impostormanhunt.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import shray.us.impostormanhunt.structures.Game;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (Game.isOngoing() && Game.getCompetitor(event.getPlayer()) != null) {}
            event.setQuitMessage("");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Game.isOngoing() && Game.getCompetitor(event.getPlayer()) != null)
            event.setJoinMessage("");
    }
}
