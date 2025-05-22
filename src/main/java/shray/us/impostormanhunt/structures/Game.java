package shray.us.impostormanhunt.structures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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
        String title = "";
        if (runnersWon) {
            title = ChatColor.GREEN + "Runners Win!";
        } else {
            title = ChatColor.RED + "Impostors Win!";
        }
        for (Competitor competitor : competitors) {
            competitor.getPlayer().sendMessage(title);
            if (!runnersWon)
                competitor.getPlayer().playSound(competitor.getPlayer(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
        }
        Game.ongoing = false;
        competitors = null;
    }

    public static Competitor getCompetitor(Player p) {
        for (Competitor c : competitors) {
            if (c.getPlayer().equals(p)) {
                return c;
            }
        }
        return null;
    }
}
