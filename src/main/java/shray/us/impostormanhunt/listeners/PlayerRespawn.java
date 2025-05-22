package shray.us.impostormanhunt.listeners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import shray.us.impostormanhunt.Main;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

public class PlayerDeath {

    @EventHandler
    public void onPlayerDeath(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();
        Competitor competitor = Game.getCompetitor(player);
        if (competitor == null) return;
        if (Game.isOngoing()) {
            player.setGameMode(GameMode.SPECTATOR);

            if (competitor.isImpostor()) {
                player.sendMessage(Main.prefix + "You will respawn in 30 seconds.");
            } else {
                player.sendMessage(Main.prefix + "You have been eliminated. You can now speak with other spectators "
                        + "in text or voice chat.");
            }
        }
    }
}
