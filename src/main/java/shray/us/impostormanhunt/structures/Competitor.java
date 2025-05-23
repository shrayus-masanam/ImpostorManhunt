package shray.us.impostormanhunt.structures;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Competitor {

    private UUID playerUUID;
    private boolean impostor;
    private boolean eliminated;

    public Competitor(Player player, boolean impostor) {
        this.playerUUID = player.getUniqueId();
        this.impostor = impostor;
        this.eliminated = false;

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
}
