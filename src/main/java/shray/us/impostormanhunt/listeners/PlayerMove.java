package shray.us.impostormanhunt.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SPECTATOR) return;
        Competitor c = Game.getCompetitor(player);
        if (c == null) return;
        if (!c.isImpostor()) return;
        event.setCancelled(true);
    }
}
