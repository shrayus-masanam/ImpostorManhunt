package shray.us.impostormanhunt.structures;

import org.bukkit.entity.Player;

public class Competitor {

    private Player player;
    private boolean impostor;
    private boolean eliminated;

    public Competitor(Player player, boolean impostor) {
        this.player = player;
        this.impostor = impostor;
        this.eliminated = false;
    }

    public Player getPlayer() {
        return player;
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
