package ch.minepvp.spout.hardcoregames.world;

import ch.minepvp.spout.hardcoregames.Game;
import ch.minepvp.spout.hardcoregames.HardCoreGames;
import com.sun.javadoc.AnnotationDesc;
import org.spout.api.generator.Populator;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.object.VanillaObjects;

import java.util.Random;

public class WallPopulator extends Populator{

    Game game;

    public WallPopulator( Game game ) {
        this.game = game;
    }

    @Override
    public void populate(Chunk chunk, Random random) {

        if ( chunk.getY() > 8) {
            return;
        }

        Point spawnPoint = chunk.getWorld().getSpawnPoint().getPosition();

        HardCoreGames.getInstance().getLogger().info("Distance : " + spawnPoint.distance( chunk.getBase() ) + " - ChunkRadius : " + (game.getChunkRadius() * 16));


        if ( spawnPoint.distance( chunk.getBase() ) <= (game.getChunkRadius() * 16) ) {


            HardCoreGames.getInstance().getLogger().info("WallPopulator");

            if ( chunk.getZ() == (spawnPoint.getChunkZ() + game.getChunkRadius()) ) {

                HardCoreGames.getInstance().getLogger().info("WallPopulator 1");
                generateWallHorizontal(chunk);

            }

            // Vertical mines
            if ( chunk.getZ() == -(spawnPoint.getChunkZ() + game.getChunkRadius()) ) {

                HardCoreGames.getInstance().getLogger().info("WallPopulator 2");
                generateWallHorizontal2(chunk);

            }

            // Horizontal plus
            if ( chunk.getX() == (spawnPoint.getChunkX() + game.getChunkRadius()) ) {

                HardCoreGames.getInstance().getLogger().info("WallPopulator 3");
                generateWallVertical(chunk);

            }

            // Horizontal mines
            if ( chunk.getX() == -(spawnPoint.getChunkX() + game.getChunkRadius()) ) {

                HardCoreGames.getInstance().getLogger().info("WallPopulator 4");
                generateWallVertical2(chunk);

            }

        }










        /*
        // Vertical plus
        if ( chunk.getZ() == game.getChunkRadius() && ( chunk.getX() < game.getChunkRadius() || chunk.getX() > -game.getChunkRadius() ) ) {

            //HardCoreGames.getInstance().getLogger().info("WallPopulator 1");
            generateWallHorizontal(chunk);

        }

        // Vertical mines
        if ( chunk.getZ() == -game.getChunkRadius() && ( chunk.getX() < game.getChunkRadius() || chunk.getX() > -game.getChunkRadius() ) ) {

            //HardCoreGames.getInstance().getLogger().info("WallPopulator 2");
            generateWallHorizontal2(chunk);

        }

        // Horizontal plus
        if ( chunk.getX() == game.getChunkRadius() && ( chunk.getZ() < game.getChunkRadius() || chunk.getZ() > -game.getChunkRadius() ) ) {

            //HardCoreGames.getInstance().getLogger().info("WallPopulator 3");
            generateWallVertical(chunk);

        }

        // Horizontal mines
        if ( chunk.getX() == -game.getChunkRadius() && ( chunk.getZ() < game.getChunkRadius() || chunk.getZ() > -game.getChunkRadius() ) ) {

            //HardCoreGames.getInstance().getLogger().info("WallPopulator 4");
            generateWallVertical2(chunk);

        }
        */

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

                chunk.setBlockMaterial( x, y, z + i, VanillaMaterials.GLOWSTONE_BLOCK, (short) 0, chunk.getWorld() );

            }

        }

    }

    private void generateWallVertical2( Chunk chunk ) {

        Integer x = chunk.getBlockX();
        Integer z = chunk.getBlockZ();

        for ( int i = 0; i <= 15; i++ ) {

            for ( int y = 0; y <= 15; y++ ) {

                chunk.setBlockMaterial( x, y, z + i, VanillaMaterials.GLASS, (short) 0, chunk.getWorld() );

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

                chunk.setBlockMaterial( x + i, y, z, VanillaMaterials.DIRT, (short) 0, chunk.getWorld() );

            }

        }

    }

    private void generateWallHorizontal2( Chunk chunk ) {

        Integer x = chunk.getBlockX();
        Integer z = chunk.getBlockZ();

        for ( int i = 0; i <= 15; i++ ) {

            for ( int y = 0; y <= 15; y++ ) {

                chunk.setBlockMaterial( x + i, y, z, VanillaMaterials.BEDROCK, (short) 0, chunk.getWorld() );

            }

        }

    }

}
