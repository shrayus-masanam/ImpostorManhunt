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
        } else if (event.getEntity() instanceof Player) {
            Competitor competitor = Game.getCompetitor((Player) event.getEntity());
            if (competitor == null) return;
            if (!competitor.isImpostor())
                competitor.eliminate();
            boolean allEliminated = true;
            for (Player player : Bukkit.getOnlinePlayers()) {
                competitor = Game.getCompetitor((Player) event.getEntity());
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
