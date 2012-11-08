package ch.minepvp.spout.hardcoregames.world;

import ch.minepvp.spout.hardcoregames.Game;
import org.spout.api.generator.Populator;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.vanilla.material.VanillaMaterials;

import java.util.Random;

public class WallPopulator extends Populator{

    Game game;

    Point p1 = null;
    Point p2 = null;

    public WallPopulator( Game game ) {
        this.game = game;
    }

    @Override
    public void populate(Chunk chunk, Random random) {

        // Calculate the Points
        if ( p1 == null && p2 == null) {
            calculateP1AndP2();
        }

        // Wall height in Chunks
        if ( chunk.getY() > 6) {
            return;
        }

        // Is the Chunk between the two Points?
        if ( chunk.getX() >= p1.getX() && chunk.getX() <= p2.getX() &&
             chunk.getZ() >= p1.getZ() && chunk.getZ() <= p2.getZ() ) {

            //HardCoreGames.getInstance().getLogger().info("Generating Wall");

            // Vertical plus
            if ( chunk.getZ() == p1.getZ() ) {

                //HardCoreGames.getInstance().getLogger().info("WallPopulator 1");
                generateWallHorizontal(chunk);

            }

            // Vertical mines
            if ( chunk.getZ() == p2.getZ() ) {

                //HardCoreGames.getInstance().getLogger().info("WallPopulator 2");
                generateWallHorizontal(chunk);

            }

            // Horizontal plus
            if ( chunk.getX() == p1.getX() ) {

                //HardCoreGames.getInstance().getLogger().info("WallPopulator 3");
                generateWallVertical(chunk);

            }

            // Horizontal mines
            if ( chunk.getX() == p2.getX() ) {

                //HardCoreGames.getInstance().getLogger().info("WallPopulator 4");
                generateWallVertical(chunk);

            }

        }

    }

    /**
     * Generate a Wall from North to South
     *
     * @param chunk
     */
    private void generateWallVertical( Chunk chunk ) {

        Integer x = chunk.getBlockX();
        Integer z = chunk.getBlockZ();

        for ( int i = 0; i <= 15; i++ ) {

            for ( int y = 0; y <= 15; y++ ) {

                chunk.setBlockMaterial( x, y, z + i, VanillaMaterials.BEDROCK, (short) 0, null );

            }

        }

    }

    /**
     * Generate a Wall from East to West
     *
     * @param chunk
     */
    private void generateWallHorizontal( Chunk chunk ) {

        Integer x = chunk.getBlockX();
        Integer z = chunk.getBlockZ();

        for ( int i = 0; i <= 15; i++ ) {

            for ( int y = 0; y <= 15; y++ ) {

                chunk.setBlockMaterial( x + i, y, z, VanillaMaterials.BEDROCK, (short) 0, null );

            }

        }

    }

    private void calculateP1AndP2() {

        int x = game.getWorld().getSpawnPoint().getPosition().getChunkX() - game.getChunkRadius();
        int z = game.getWorld().getSpawnPoint().getPosition().getChunkZ() - game.getChunkRadius();

        p1 = new Point(game.getWorld(), x,0,z);


        x = game.getWorld().getSpawnPoint().getPosition().getChunkX() + game.getChunkRadius();
        z = game.getWorld().getSpawnPoint().getPosition().getChunkZ() + game.getChunkRadius();

        p2 = new Point(game.getWorld(), x,0,z);

    }

}
