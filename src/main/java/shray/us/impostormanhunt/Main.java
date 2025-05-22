package shray.us.impostormanhunt;

import io.papermc.paper.command.brigadier.BasicCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import shray.us.impostormanhunt.commands.Announce;
import shray.us.impostormanhunt.commands.ImpostorManhunt;
import shray.us.impostormanhunt.commands.Lost;

import java.util.logging.Logger;

public final class Main extends JavaPlugin implements CommandExecutor {

    private static Main instance;
    public static Main getInstance() {
        return instance;
    }
    private Logger logger;
    private CommandExecutor[] commands;
    public static final String prefix = "[" + ChatColor.YELLOW + "ImpostorManHunt" + ChatColor.RESET + "] ";

    public void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        PluginManager manager = getServer().getPluginManager();
        for (Listener listener : listeners) {
            manager.registerEvents(listener, plugin);
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = getLogger();
        commands = new CommandExecutor[]{new Announce(), new ImpostorManhunt(), new Lost()};
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
        logger = null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        for (CommandExecutor commandExecutor : commands) {
            if (commandExecutor.getClass().getSimpleName().equals(label)) {
                return commandExecutor.onCommand(commandSender, command, label, args);
            }
        }
        return false;
    }

    public Logger logger() {
        return logger;
    }

}
