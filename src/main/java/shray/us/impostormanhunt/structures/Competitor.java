package shray.us.impostormanhunt.structures;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Competitor {

    private UUID playerUUID;
    private boolean impostor;
    private boolean eliminated;
    private String tracking;

    public Competitor(Player player, boolean impostor) {
        this.playerUUID = player.getUniqueId();
        this.impostor = impostor;
        this.eliminated = false;
        this.tracking = impostor ? "closest" : "";

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setExp(0);
        player.setLevel(0);
        player.clearActivePotionEffects();
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvulnerable(false);
        player.setAllowFlight(false);
    }

    public Player getPlayer() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().equals(playerUUID))
                return player;
        }
        return null;
    }

    public boolean isImpostor() {
        return impostor;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void eliminate() {
        this.eliminated = true;
    }

    private void setActionBar(Player player, String text) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
    }


    // from nms
    private static float wrapDegrees(float value) {
        float f = value % 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

    private static String getRelativeDir(Location source, Location destination) {
        // check if on same block
        double dx = destination.getX() - source.getX();
        double dz = destination.getZ() - source.getZ();
        if (dx * dx + dz * dz <= 1.0) return "●";

        // get difference in yaws
        double dest_yaw = Math.toDegrees(Math.atan2(-dx, dz));
        double src_yaw = source.getYaw();
        double diff = wrapDegrees((float) (dest_yaw - src_yaw));

        // map to direction
        if (diff < -157.5 || diff >= 157.5) return "⬇";
        if (diff < -112.5) return "⬋";
        if (diff < -67.5)  return "⬅";
        if (diff < -22.5)  return "⬉";
        if (diff < 22.5)  return "⬆";
        if (diff < 67.5)  return "⬈";
        if (diff < 112.5)  return "➡";
        return "⬊";
    }

    public void trackTarget() {
        if (!impostor) return;
        if (tracking == null || tracking.equals("")) return;
        Player p = Bukkit.getPlayer(playerUUID);
        if (p == null) return;
        switch (tracking) {
            case "closest":
                Player target = null;
                for (Player other : Bukkit.getOnlinePlayers()) {
                    Competitor c = Game.getCompetitor(other);
                    if (c == null || c.isImpostor() || c.isEliminated()) continue;
                    if (!other.getLocation().getWorld().equals(p.getLocation().getWorld())) continue;
                    if (target == null
                            || other.getLocation().distance(p.getLocation()) <
                                target.getLocation().distance(p.getLocation()))
                        target = other;
                }
                if (target == null)
                    setActionBar(p, ChatColor.RED + "No runners are in your dimension.");
                else
                    setActionBar(p, ChatColor.AQUA + "" + ChatColor.BOLD + getRelativeDir(p.getLocation(),
                            target.getLocation()));
                break;
            case "portal":
                Location end_portal = Game.getEndPortal();
                if (end_portal == null || !end_portal.getWorld().equals(p.getLocation().getWorld()))
                    setActionBar(p, ChatColor.RED + "No End portals have been opened in your dimension.");
                else
                    setActionBar(p, ChatColor.AQUA + "" + ChatColor.BOLD + getRelativeDir(p.getLocation(),
                            end_portal));
                break;
            default:
                Player other = Bukkit.getPlayer(tracking);
                Competitor c = Game.getCompetitor(other);
                if (c == null)
                    setActionBar(p, ChatColor.RED + "Cannot track " + tracking + " because they are not playing.");
                else if (c.isEliminated())
                    setActionBar(p, ChatColor.RED + "Cannot track " + tracking + " because they are eliminated.");
                else if (!p.getLocation().getWorld().equals(other.getLocation().getWorld()))
                    setActionBar(p, ChatColor.RED + "Cannot track " + tracking
                            + " because they are in another dimension.");
                else
                    setActionBar(p, ChatColor.AQUA + "" + ChatColor.BOLD + getRelativeDir(p.getLocation(),
                            other.getLocation()));
        }
    }
}
