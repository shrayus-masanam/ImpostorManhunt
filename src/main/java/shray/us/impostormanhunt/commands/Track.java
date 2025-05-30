package shray.us.impostormanhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

public class Track implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }
        Competitor c = Game.getCompetitor(player);
        if (c == null || !c.isImpostor()) {
            player.sendMessage(ChatColor.RED + "You must be an impostor to use this command.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("closest")) {
            c.setTracking("closest");
            player.sendMessage(ChatColor.GREEN
                    + "Your compass is now pointing to the closest player.");
            return true;
        } else if (args[0].equalsIgnoreCase("portal")) {
            c.setTracking("portal");
            player.sendMessage(ChatColor.GREEN
                    + "Your compass is now pointing to the most recently opened End portal.");
            return true;
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().equalsIgnoreCase(args[0])) {
                    c.setTracking(p.getName());
                    player.sendMessage(ChatColor.GREEN + "Your compass is now pointing to " + p.getName() + ".");
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Couldn't find a player named \"" + args[0] + "\".");
            return true;
        }
    }
}
