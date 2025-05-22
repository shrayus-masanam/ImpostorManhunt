package shray.us.impostormanhunt.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Lost implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }
        if (player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage(ChatColor.RED + "You can't use this command as a spectator.");
            return true;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.YELLOW + p.getName() + ChatColor.RESET + " is lost and needs help!");
        }

        return true;
    }
}
