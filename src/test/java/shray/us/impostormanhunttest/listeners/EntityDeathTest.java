package shray.us.impostormanhunttest.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.damage.DamageSourceMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import shray.us.impostormanhunt.listeners.EntityDeath;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EntityDeathTest {

    private ServerMock server;
    private Entity dragon;
    private Player bystander;
    private Entity irrelevant;
    private Player[] players;

    @Before
    public void setUp() {
        server = MockBukkit.mock();

        WorldMock world1 = server.addSimpleWorld("world_the_end");
        world1.setEnvironment(World.Environment.THE_END);
        dragon = world1.createEntity(new Location(world1, 0, 100, 0), EnderDragon.class);
        irrelevant = world1.createEntity(new Location(world1, 0, 4, 0), Silverfish.class);
        players = new Player[]{server.addPlayer(), server.addPlayer(), server.addPlayer()};
        Game.start();
    }

    @After
    public void tearDown() {
        Game.stop();
        MockBukkit.unmock();
        dragon = null;
        bystander = null;
        players = null;
        server = null;
    }

    @Test
    public void testDragonDeath() {
        // killing the dragon ends the game
        LivingEntity lDrag = (LivingEntity) dragon;
        lDrag.setHealth(0);
        server.getPluginManager().assertEventFired(EntityDeathEvent.class);
        DamageSource source = new DamageSourceMock(null, null, null,
                null);
        List<ItemStack> drops = List.of();
        EntityDeathEvent event = new EntityDeathEvent(lDrag, source, drops);
        new EntityDeath().onEntityDeath(event);
        assertFalse(Game.isOngoing());
    }

    @Test
    public void testIrrelevantDeath() {
        // killing irrelevant mobs shouldn't affect the game

        bystander = server.addPlayer(); // not in competition
        LivingEntity lBystander = (LivingEntity) bystander;
        lBystander.setHealth(0);
        server.getPluginManager().assertEventFired(EntityDeathEvent.class);
        DamageSource source = new DamageSourceMock(null, null, null,
                null);
        List<ItemStack> drops = List.of();
        EntityDeathEvent event = new EntityDeathEvent(lBystander, source, drops);
        new EntityDeath().onEntityDeath(event);
        assertTrue(Game.isOngoing());

        LivingEntity lIrrelevant = (LivingEntity) irrelevant;
        lIrrelevant.setHealth(0);
        server.getPluginManager().assertEventFired(EntityDeathEvent.class);
        event = new EntityDeathEvent(lIrrelevant, source, drops);
        new EntityDeath().onEntityDeath(event);
        assertTrue(Game.isOngoing());
    }

    @Test
    public void testRunnerDeath() {
        bystander = server.addPlayer(); // not in competition

        ArrayList<Player> runners = new ArrayList<>();
        for (Player player : players) {
            Competitor c = Game.getCompetitor(player);
            assertNotNull(c);
            if (!c.isImpostor())
                runners.add(player);
        }
        assertTrue(runners.size() >= 2);

        // kill 1 runner
        runners.getFirst().setHealth(0);
        server.getPluginManager().assertEventFired(EntityDeathEvent.class);
        DamageSource source = new DamageSourceMock(null, null, null,
                null);
        List<ItemStack> drops = List.of();
        EntityDeathEvent event = new EntityDeathEvent(runners.getFirst(), source, drops);
        new EntityDeath().onEntityDeath(event);
        assertTrue(Game.isOngoing());

        // kill irrelevant player
        bystander.setHealth(0);
        server.getPluginManager().assertEventFired(EntityDeathEvent.class);
        event = new EntityDeathEvent(bystander, source, drops);
        new EntityDeath().onEntityDeath(event);
        assertTrue(Game.isOngoing());

        // kill last runner
        runners.get(1).setHealth(0);
        server.getPluginManager().assertEventFired(EntityDeathEvent.class);
        event = new EntityDeathEvent(runners.get(1), source, drops);
        new EntityDeath().onEntityDeath(event);
        assertFalse(Game.isOngoing());
    }

    @Test
    public void testImpostorDeath() {
        ArrayList<Player> impostors = new ArrayList<>();
        for (Player player : players) {
            Competitor c = Game.getCompetitor(player);
            assertNotNull(c);
            if (c.isImpostor()) {
                impostors.add(player);
            }
        }
        assertFalse(impostors.isEmpty());

        // game shouldn't end on impostor death
        DamageSource source = new DamageSourceMock(null, null, null,
                null);
        List<ItemStack> drops = List.of();
        for (Player player : impostors) {
            player.setHealth(0);
            server.getPluginManager().assertEventFired(EntityDeathEvent.class);
            EntityDeathEvent event = new EntityDeathEvent(player, source, drops);
            new EntityDeath().onEntityDeath(event);
            assertTrue(Game.isOngoing());
        }
    }

}
