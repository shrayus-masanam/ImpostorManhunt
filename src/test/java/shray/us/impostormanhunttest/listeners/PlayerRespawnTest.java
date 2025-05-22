package shray.us.impostormanhunttest.listeners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.damage.DamageSourceMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import shray.us.impostormanhunt.listeners.EntityDeath;
import shray.us.impostormanhunt.listeners.PlayerRespawn;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerRespawnTest {

    private ServerMock server;
    private Player bystander;
    private Player[] players;

    @Before
    public void setUp() {
        server = MockBukkit.mock();

        WorldMock world1 = server.addSimpleWorld("world");
        players = new Player[]{server.addPlayer(), server.addPlayer(), server.addPlayer()};
        Game.start();
    }

    @After
    public void tearDown() {
        Game.stop();
        MockBukkit.unmock();
        bystander = null;
        players = null;
        server = null;
    }

    @Test
    public void testRunnerRespawn() {
        bystander = server.addPlayer(); // not in competition

        ArrayList<Player> runners = new ArrayList<>();
        ArrayList<Player> impostors = new ArrayList<>();
        for (Player player : players) {
            Competitor c = Game.getCompetitor(player);
            assertNotNull(c);
            if (!c.isImpostor())
                runners.add(player);
            else
                impostors.add(player);
        }
        assertFalse(runners.isEmpty());
        assertFalse(impostors.isEmpty());

        // respawn 1 runner

        // Unimplemented in MockBukkit
        //runners.getFirst().spawnAt(runners.getFirst().getLocation());
        //server.getPluginManager().assertEventFired(PlayerPostRespawnEvent.class);

        // fire death even to eliminate the player
        DamageSource source = new DamageSourceMock(null, null, null,
                null);
        List<ItemStack> drops = List.of();
        EntityDeathEvent event = new EntityDeathEvent(runners.getFirst(), source, drops);
        new EntityDeath().onEntityDeath(event);

        PlayerPostRespawnEvent event2 = new PlayerPostRespawnEvent(runners.getFirst(), runners.getFirst().getLocation(),
                false, false, false, PlayerRespawnEvent.RespawnReason.DEATH);
        new PlayerRespawn().onPlayerDeath(event2);
        assertSame(GameMode.SPECTATOR, runners.getFirst().getGameMode());
        Competitor c = Game.getCompetitor(runners.getFirst());
        assertNotNull(c);
        assertTrue(c.isEliminated());

        // respawn irrelevant player
        bystander.setGameMode(GameMode.SURVIVAL);

        // Unimplemented in MockBukkit
        //bystander.spawnAt(runners.getFirst().getLocation());
        //server.getPluginManager().assertEventFired(PlayerPostRespawnEvent.class);


        event = new EntityDeathEvent(bystander, source, drops);
        new EntityDeath().onEntityDeath(event);
        event2 = new PlayerPostRespawnEvent(bystander, runners.getFirst().getLocation(),
                false, false, false, PlayerRespawnEvent.RespawnReason.DEATH);
        new PlayerRespawn().onPlayerDeath(event2);

        c = Game.getCompetitor(bystander);
        assertNull(c);
        assertSame(GameMode.SURVIVAL, bystander.getGameMode());

        // respawn impostor

        // Unimplemented in MockBukkit
        //impostors.getFirst().spawnAt(runners.getFirst().getLocation());
        //server.getPluginManager().assertEventFired(PlayerPostRespawnEvent.class);

        event = new EntityDeathEvent(impostors.getFirst(), source, drops);
        new EntityDeath().onEntityDeath(event);
        event2 = new PlayerPostRespawnEvent(impostors.getFirst(), runners.getFirst().getLocation(),
                false, false, false, PlayerRespawnEvent.RespawnReason.DEATH);
        new PlayerRespawn().onPlayerDeath(event2);
        assertSame(GameMode.SPECTATOR, runners.getFirst().getGameMode());
        c = Game.getCompetitor(impostors.getFirst());
        assertNotNull(c);
        assertFalse(c.isEliminated());
        server.getScheduler().performTicks(20L * 30);
        assertSame(GameMode.SURVIVAL, impostors.getFirst().getGameMode());

    }

}
