package shray.us.impostormanhunt.structures;

import org.bukkit.*;
import org.bukkit.entity.Player;
import shray.us.impostormanhunt.Main;

import java.util.Collection;
import java.util.HashSet;

public class Game {
    private static HashSet<Competitor> competitors;
    private static boolean ongoing = false;

    public static boolean isOngoing() {
        return ongoing;
    }

    public static void start() {
        competitors = new HashSet<>();
        Object[] players = Bukkit.getOnlinePlayers().toArray();
        int impostor_idx = (int)(Math.random() * players.length);
        for (int i = 0; i < players.length; i++) {
            Player p = (Player)players[i];
            competitors.add(new Competitor(p, i == impostor_idx));
            p.sendMessage(Main.prefix + "You are " + (i == impostor_idx ? "the "
                    + ChatColor.RED + "impostor" : "a " + ChatColor.GREEN + "runner") + ChatColor.RESET + "!");
        }
        Game.ongoing = true;
    }
    public static void stop(boolean runnersWon) {
        String title;
        if (runnersWon) {
            title = ChatColor.GREEN + "Runners Win!";
        } else {
            title = ChatColor.RED + "Impostors Win!";
        }
        for (Competitor competitor : competitors) {
            Player p = competitor.getPlayer();
            if (p == null) continue;
            p.sendTitle(title, "");
            p.setGameMode(GameMode.SURVIVAL);
            p.setInvulnerable(true);
            if (!runnersWon) {
                p.playSound(p, Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
            }
        }
        Game.ongoing = false;
        competitors = null;
    }

    public static void stop() {
        // forceful stop
        Game.ongoing = false;
        competitors = null;
    }

    public static Competitor getCompetitor(Player p) {
        if (competitors == null) return null;
        for (Competitor c : competitors) {
            if (c.getPlayer().equals(p)) {
                return c;
            }
        }
        return null;
    }
}
