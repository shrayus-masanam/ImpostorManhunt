package shray.us.impostormanhunt.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import shray.us.impostormanhunt.structures.Game;

public class ImpostorManhunt implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            Game.start();
        }

        return true;
    }
}
