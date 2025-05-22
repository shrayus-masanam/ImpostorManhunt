package shray.us.impostormanhunttest.utils;

import org.bukkit.World;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import shray.us.impostormanhunt.utils.WorldName;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WorldNameTest {

    private ServerMock server;

    @Before
    public void setUp() {
        server = MockBukkit.mock();

        WorldMock world1 = server.addSimpleWorld("world");;
        world1.setEnvironment(World.Environment.NORMAL);

        WorldMock world2 = server.addSimpleWorld("world_nether");;
        world2.setEnvironment(World.Environment.NETHER);

        WorldMock world3 = server.addSimpleWorld("world_the_end");;
        world3.setEnvironment(World.Environment.THE_END);

        WorldMock world4 = server.addSimpleWorld("limbo");;
        world4.setEnvironment(World.Environment.CUSTOM);
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
        server = null;
    }

    @Test
    public void testToPretty() {
        World overworld = server.getWorld("world");
        assertNotNull(overworld);
        assertEquals("Overworld", WorldName.toPretty(overworld));

        World nether = server.getWorld("world_nether");
        assertNotNull(nether);
        assertNotEquals("Overworld", WorldName.toPretty(nether));
        assertEquals("Nether", WorldName.toPretty(nether));

        World end = server.getWorld("world_the_end");
        assertNotNull(end);
        assertNotEquals("Overworld", WorldName.toPretty(end));
        assertNotEquals("Nether", WorldName.toPretty(end));
        assertEquals("End", WorldName.toPretty(end));

        World limbo = server.getWorld("limbo");
        assertNotNull(limbo);
        assertNotEquals("Overworld", WorldName.toPretty(limbo));
        assertNotEquals("Nether", WorldName.toPretty(limbo));
        assertNotEquals("End", WorldName.toPretty(limbo));
        assertEquals("limbo", WorldName.toPretty(limbo));
    }

    @Test
    public void testToDimension() {
        World overworld = server.getWorld("world");
        assertNotNull(overworld);
        assertEquals("overworld", WorldName.toDimension(overworld));

        World nether = server.getWorld("world_nether");
        assertNotNull(nether);
        assertNotEquals("overworld", WorldName.toDimension(nether));
        assertEquals("the_nether", WorldName.toDimension(nether));

        World end = server.getWorld("world_the_end");
        assertNotNull(end);
        assertNotEquals("overworld", WorldName.toDimension(end));
        assertNotEquals("the_nether", WorldName.toDimension(end));
        assertEquals("the_end", WorldName.toDimension(end));

        World limbo = server.getWorld("limbo");
        assertNotNull(limbo);
        assertNotEquals("overworld", WorldName.toDimension(limbo));
        assertNotEquals("the_nether", WorldName.toDimension(limbo));
        assertNotEquals("the_end", WorldName.toDimension(limbo));
        assertEquals("limbo", WorldName.toDimension(limbo));
    }
}
