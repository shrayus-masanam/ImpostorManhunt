package shray.us.impostormanhunt.utils;

import org.bukkit.World;

public class WorldName {

    public static String toPretty(World world) {
        World.Environment env = world.getEnvironment();
        return switch (env) {
            case NORMAL -> "Overworld";
            case NETHER -> "Nether";
            case THE_END -> "End";
            default ->
                // custom datapack envs
                    world.getName();
        };
    }

    public static String toDimension(World world) {
        World.Environment env = world.getEnvironment();
        return switch (env) {
            case NORMAL -> "overworld";
            case NETHER -> "the_nether";
            case THE_END -> "the_end";
            default ->
                // custom datapack envs
                    world.getName();
        };
    }

}
