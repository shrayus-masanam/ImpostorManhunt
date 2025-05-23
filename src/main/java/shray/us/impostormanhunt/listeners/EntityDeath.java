package shray.us.impostormanhunt.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

public class EntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            Game.stop(true);
        } else if (event.getEntity() instanceof Player player) {
            Competitor competitor = Game.getCompetitor(player);
            if (competitor == null) return;
            if (!competitor.isImpostor())
                competitor.eliminate();
            boolean allEliminated = true;
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                competitor = Game.getCompetitor(player2);
                if (competitor == null || competitor.isImpostor()) continue;
                if (!competitor.isEliminated()) {
                    allEliminated = false;
                    break;
                }
            }
            if (allEliminated)
                Game.stop(false);
        }
    }
}
