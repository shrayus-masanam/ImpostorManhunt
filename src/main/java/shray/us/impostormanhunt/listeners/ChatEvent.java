package shray.us.impostormanhunt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

public class ChatEvent {

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        Competitor c = Game.getCompetitor(event.getPlayer());
        if (c == null) return;
        if (!c.isEliminated()) {
            event.getRecipients().clear();
            event.setCancelled(true);
        } else {
            for (Player p : event.getRecipients()) {
                Competitor c2 = Game.getCompetitor(p);
                if (c2 == null) continue;
                if (!c2.isEliminated())
                    event.getRecipients().remove(p);
            }
        }
    }
}
