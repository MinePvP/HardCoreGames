package ch.minepvp.spout.hardcoregames.world;

import ch.minepvp.spout.hardcoregames.Game;
import org.spout.api.generator.Populator;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.vanilla.material.VanillaMaterials;

import java.util.Random;

public class WallPopulator extends Populator{

    Game game;

    public WallPopulator( Game game ) {
        this.game = game;
    }

    @Override
    public void populate(Chunk chunk, Random random) {

        // Wall height in Chunks
        if ( chunk.getY() > 6) {
            return;
        }

        // Is the Chunk between the two Points?
        if ( chunk.getX() >= game.getP1().getChunkX() && chunk.getX() <= game.getP2().getChunkX() &&
             chunk.getZ() >= game.getP1().getChunkZ() && chunk.getZ() <= game.getP2().getChunkZ() ) {

            //HardCoreGames.getInstance().getLogger().info("Generating Wall");

            // Vertical plus
            if ( chunk.getZ() == game.getP1().getChunkZ() ) {

                //HardCoreGames.getInstance().getLogger().info("WallPopulator 1");
                generateWallHorizontal(chunk);

            }

            // Vertical mines
            if ( chunk.getZ() == game.getP2().getChunkZ() ) {

                //HardCoreGames.getInstance().getLogger().info("WallPopulator 2");
                generateWallHorizontal(chunk);

            }

            // Horizontal plus
            if ( chunk.getX() == game.getP1().getChunkX() ) {

                //HardCoreGames.getInstance().getLogger().info("WallPopulator 3");
                generateWallVertical(chunk);

            }

            // Horizontal mines
            if ( chunk.getX() == game.getP2().getChunkX() ) {

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

}
