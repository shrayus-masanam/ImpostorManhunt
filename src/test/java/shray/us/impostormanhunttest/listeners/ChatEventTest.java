package shray.us.impostormanhunttest.listeners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.damage.DamageSourceMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import shray.us.impostormanhunt.listeners.ChatEvent;
import shray.us.impostormanhunt.listeners.EntityDeath;
import shray.us.impostormanhunt.listeners.PlayerRespawn;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ChatEventTest {

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

    private void populateRecipients(Set<Player> recipients) {
        recipients.clear();
        recipients.addAll(Arrays.asList(players));
        if (bystander != null)
            recipients.add(bystander);
    }

    @Test
    public void testCompetitorChat() {

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
        assertTrue(runners.size() >= 2);
        assertFalse(impostors.isEmpty());

        // nobody eliminated, message should go to nobody
        Set<Player> recipients = new HashSet<>();
        populateRecipients(recipients);
        AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, runners.getFirst(), "hi",
                recipients);
        new ChatEvent().onMessage(event);
        assertTrue(recipients.isEmpty());

         // eliminate 1 runner, message should go only to themselves
        Competitor c = Game.getCompetitor(runners.getFirst());
        assertNotNull(c);
        c.eliminate();
        populateRecipients(recipients);
        new ChatEvent().onMessage(event);
        assertEquals(1, recipients.size());
        assertEquals(c.getPlayer(), recipients.iterator().next());

        // add player not in competition, message can go to them and must go to the eliminated runner
        bystander = server.addPlayer();
        populateRecipients(recipients);
        new ChatEvent().onMessage(event);
        assertEquals(2, recipients.size());
        assertTrue(recipients.contains(bystander));
        assertTrue(recipients.contains(c.getPlayer()));

        // eliminate a second runner, now the message will go to both runners and the non-competing player
        populateRecipients(recipients);
        c = Game.getCompetitor(runners.get(1));
        assertNotNull(c);
        c.eliminate();
        new ChatEvent().onMessage(event);
        assertEquals(3, recipients.size());

        // players not in the competition shouldn't be affected by chat limitation
        populateRecipients(recipients);
        c = Game.getCompetitor(bystander);
        assertNull(c);
        event = new AsyncPlayerChatEvent(true, bystander, "hi",
                recipients);
        new ChatEvent().onMessage(event);
        assertEquals(4, recipients.size());
    }

}
