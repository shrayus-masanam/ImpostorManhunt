package shray.us.impostormanhunt.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import shray.us.impostormanhunt.Main;
import shray.us.impostormanhunt.structures.Game;

public class EndPortalOpen implements Listener {

    private final int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.ENDER_EYE) return;
        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.END_PORTAL_FRAME) return;
        World world = block.getWorld();
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (int[] direction : directions) {
                    Block other = world.getBlockAt(block.getX() + direction[0], block.getY(),
                            block.getZ() + direction[1]);
                    if (other.getType() != Material.END_PORTAL) continue;
                    Game.setLastEndPortal(block.getLocation());
                    break;
                }
            }
        }, 20L);
    }

}
