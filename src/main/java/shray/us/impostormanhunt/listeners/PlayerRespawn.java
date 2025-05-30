package shray.us.impostormanhunt.listeners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import shray.us.impostormanhunt.Main;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

public class PlayerRespawn implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();
        Competitor competitor = Game.getCompetitor(player);
        if (competitor == null) return;
        if (Game.isOngoing()) {
            player.setGameMode(GameMode.SPECTATOR);

            if (competitor.isImpostor()) {
                player.sendMessage(Main.prefix + "You will respawn in 30 seconds.");
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        player.setGameMode(GameMode.SURVIVAL);
                    }
                }, 20L * 30);
            } else {
                player.sendMessage(Main.prefix + "You have been eliminated. You can now speak with other spectators "
                        + "in text or voice chat.\nYou can use /goto <username> to teleport to anyone.");
            }
        }
    }
}
