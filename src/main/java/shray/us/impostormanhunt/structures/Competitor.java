package shray.us.impostormanhunt.structures;

import org.bukkit.entity.Player;

public class Competitor {

    private Player player;
    private boolean impostor;

    public Competitor(Player player, boolean impostor) {
        this.player = player;
        this.impostor = impostor;
    }

}
