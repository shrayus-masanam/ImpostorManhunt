package shray.us.impostormanhunt;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static Main getInstance() {
        return instance;
    }
    private Logger logger;

    public void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        PluginManager manager = getServer().getPluginManager();
        for (Listener listener : listeners) {
            manager.registerEvents(listener, plugin);
        }
    }

    private void registerCommands() {
        // TODO
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = getLogger();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
        logger = null;
    }

    public Logger logger() {
        return logger;
    }
}
