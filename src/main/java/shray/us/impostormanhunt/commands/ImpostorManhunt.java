package shray.us.impostormanhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import shray.us.impostormanhunt.structures.Game;
import shray.us.impostormanhunt.utils.WorldName;

public class ImpostorManhunt implements CommandExecutor {

    private final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();


    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            Game.start();
            for (World w : Bukkit.getWorlds()) {
                Bukkit.dispatchCommand(console, "execute in "
                        + WorldName.toDimension(w) + " run gamerule announceAdvancements false");
                Bukkit.dispatchCommand(console, "execute in "
                        + WorldName.toDimension(w) + " run gamerule showDeathMessages false");
            }
            commandSender.sendMessage(ChatColor.GREEN + "Game started.");
        } else if (args[0].equalsIgnoreCase("stop")) {
            Game.stop();
            commandSender.sendMessage(ChatColor.GREEN + "Game stopped.");
        }

        return true;
    }
}
