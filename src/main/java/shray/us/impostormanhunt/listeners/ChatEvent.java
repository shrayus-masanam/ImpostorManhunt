package shray.us.impostormanhunt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

import java.util.Iterator;

public class ChatEvent {

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        Competitor c = Game.getCompetitor(event.getPlayer());
        if (c == null) return;
        if (!c.isEliminated()) {
            event.getRecipients().clear();
            event.setCancelled(true);
        } else {
            Iterator<Player> it = event.getRecipients().iterator();
            while (it.hasNext()) {
                Player p = it.next();
                Competitor c2 = Game.getCompetitor(p);
                if (c2 != null && !c2.isEliminated()) {
                    it.remove();
                }
            }
        }
    }
}
