package shray.us.impostormanhunt.structures;

import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import shray.us.impostormanhunt.Main;
import shray.us.impostormanhunt.utils.WorldName;

import java.util.HashSet;
import java.util.Random;

public class Game {
    private static HashSet<Competitor> competitors;
    private static boolean ongoing = false;
    private static BukkitTask impostor_compass;
    private static Location last_end_portal;
    private static final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    // Fisher–Yates shuffle https://stackoverflow.com/a/1520212
    private static void shuffle(Object[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Object a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static boolean isOngoing() {
        return ongoing;
    }

    public static void start() {
        if (ongoing)
            stop();
        for (World w : Bukkit.getWorlds()) {
            Bukkit.dispatchCommand(console, "execute in "
                    + WorldName.toDimension(w) + " run gamerule announceAdvancements false");
            Bukkit.dispatchCommand(console, "execute in "
                    + WorldName.toDimension(w) + " run gamerule showDeathMessages false");
            Bukkit.dispatchCommand(console, "execute in "
                    + WorldName.toDimension(w) + " run difficulty hard");
            Bukkit.dispatchCommand(console, "execute in "
                    + WorldName.toDimension(w) + " time set day");
        }
        competitors = new HashSet<>();
        Object[] players = Bukkit.getOnlinePlayers().toArray();
        shuffle(players);
        int impostor_idx = (int)(Math.random() * players.length);

        for (int i = 0; i < players.length; i++) {
            Player p = (Player)players[i];
            competitors.add(new Competitor(p, i == impostor_idx));
            p.getInventory().clear();
            p.setGameMode(GameMode.SURVIVAL);
            p.setInvulnerable(true);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.setInvulnerable(false);
                }
            }, 20L * 5);
            p.teleport(p.getWorld().getSpawnLocation()
                    .add(Math.random() * 10, 0, Math.random() * 10)
                    .toHighestLocation().add(0, 3, 0));
            p.sendTitle(ChatColor.GREEN + "Impostor Manhunt", "", 10, 20 * 5, 10);
            p.playSound(p, "entity.ender_dragon.growl", 1.0F, 1.0F);
            p.sendMessage(Main.prefix + "You are " + (i == impostor_idx ? "an "
                    + ChatColor.RED + "impostor" : "a " + ChatColor.GREEN + "runner") + ChatColor.RESET + "!");
        }

        impostor_compass = new BukkitRunnable() {
            @Override public void run() {
                for (Competitor competitor : competitors) {
                    if (competitor.isImpostor())
                        competitor.trackTarget();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 5L);

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
            //p.sendTitle(title, "");
            p.sendTitle(title, "", 10, 20 * 5, 10);
            p.setGameMode(GameMode.SURVIVAL);
            p.setInvulnerable(true);
            if (!runnersWon)
                p.playSound(p, Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
        }
        impostor_compass.cancel();
        impostor_compass = null;
        last_end_portal = null;
        Game.ongoing = false;
        competitors = null;
    }

    public static void stop() {
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

    public static void setLastEndPortal(Location at) {
        last_end_portal = at;
    }

    public static Location getEndPortal() {
        return last_end_portal;
    }
}
