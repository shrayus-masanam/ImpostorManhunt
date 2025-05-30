package shray.us.impostormanhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

public class Goto implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }
        Competitor c = Game.getCompetitor(player);
        if (c == null || !c.isEliminated()) {
            player.sendMessage(ChatColor.RED + "You must be an eliminated competitor to use this command.");
            return true;
        }
        if (args.length < 1)
            return false;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (args[0].equalsIgnoreCase(p.getName())) {
                player.teleport(p.getLocation());
                player.sendMessage(ChatColor.GREEN + "Teleported to " + p.getName() + ".");
                return true;
            }
        }
        player.sendMessage(ChatColor.RED + "Couldn't find a player named \"" + args[0] + "\".");
        return true;
    }
}
