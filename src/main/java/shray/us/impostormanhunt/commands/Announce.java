package shray.us.impostormanhunt.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Announce implements CommandExecutor {
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
        Location loc = player.getLocation();

        World.Environment env = loc.getWorld().getEnvironment();
        String world_name;
        switch (env) {
            case NORMAL:
                world_name = "Overworld";
                break;
            case NETHER:
                world_name = "Nether";
                break;
            case THE_END:
                world_name = "End";
                break;
            default:
                // custom datapack envs
                world_name = loc.getWorld().getName();
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.YELLOW + p.getName() + ChatColor.RESET + " is announcing their location in the " + world_name
                    + " at XYZ: " + loc.getBlockX() + " / " + loc.getBlockY() + " / " + loc.getBlockZ());
        }

        return true;
    }
}
