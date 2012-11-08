package ch.minepvp.spout.hardcoregames.task;

import ch.minepvp.spout.hardcoregames.config.GameDifficulty;
import ch.minepvp.spout.hardcoregames.config.GameStatus;
import ch.minepvp.spout.hardcoregames.world.WallPopulator;
import org.spout.api.chat.ChatArguments;
import org.spout.api.entity.Player;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.lang.Translation;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.OutwardIterator;

import ch.minepvp.spout.hardcoregames.Game;
import ch.minepvp.spout.hardcoregames.HardCoreGames;
import ch.minepvp.spout.hardcoregames.config.Config;
import org.spout.vanilla.component.world.VanillaSky;
import org.spout.vanilla.component.world.sky.NormalSky;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.thread.SpawnLoaderThread;
import org.spout.vanilla.world.generator.VanillaGenerator;
import org.spout.vanilla.world.generator.VanillaGenerators;
import org.spout.vanilla.world.generator.normal.NormalGenerator;

public class GenerateWorldsTask implements Runnable{
	
	private Game game;

	public GenerateWorldsTask( Game game ) {
		this.game = game;
	}
	

	@Override
	public void run() {

        // Generate World
        for (Player player : game.getPlayers() ) {

            if ( player.isOnline() ) {
                 player.sendMessage(ChatArguments.fromFormatString( Translation.tr("{{GOLD}}Starting World generation...", player) ) );
            }

        }

		generateWorld();

        /*
        // Generate Nether
		game.sendMessage("Starting Nether generation...");
		generateNether();
		*/

        // Start Game
        for ( Player player : game.getPlayers() ) {

            if ( player.isOnline() ) {
                player.sendMessage( ChatArguments.fromFormatString( Translation.tr("{{GOLD}}Starting Teleporting all Players to the new World..", player) ) );
            }

        }

        game.setStatus( GameStatus.RUNNING );
        game.getRandomSpawns();
        game.teleportPlayersToWorld();

	}

    public void generateWorld() {

        NormalGenerator normalGenerator = new NormalGenerator();
        normalGenerator.addPopulators( new WallPopulator(game) );

        World world = HardCoreGames.getInstance().getEngine().loadWorld("hcg_" + game.getOwner().getName(), normalGenerator );

        world.getDataMap().put(VanillaData.GAMEMODE, GameMode.SURVIVAL);
        world.getDataMap().put(VanillaData.DIMENSION, Dimension.NORMAL);

        if ( game.getDifficulty().equals(GameDifficulty.EASY) ) {
            world.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.EASY);

        } else if ( game.getDifficulty().equals(GameDifficulty.NORMAL) ) {
            world.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.NORMAL);

        } else if ( game.getDifficulty().equals(GameDifficulty.HARD) ) {
            world.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.HARD);

        } else if ( game.getDifficulty().equals(GameDifficulty.HARDCORE) ) {
            world.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.HARDCORE);
        }

        // Grab safe spawn if newly created world.
        world.setSpawnPoint(new Transform( new Point( normalGenerator.getSafeSpawn(world) ), Quaternion.IDENTITY, Vector3.ONE) );

        // Preload Chunks
        final int diameter = (game.getChunkRadius() << 1) + 1;
        final int total = (diameter * diameter * diameter) / 6;
        final int progressStep = total / 10;

        SpawnLoaderThread[] loaderThreads = new SpawnLoaderThread[16];

        for (int i = 0; i < 16; i++) {
            loaderThreads[i] = new SpawnLoaderThread(total, progressStep, "Generating");
        }

        // Initialize the first chunks
        Point point = world.getSpawnPoint().getPosition();
        int cx = point.getBlockX() >> Chunk.BLOCKS.BITS;
        int cy = point.getBlockY() >> Chunk.BLOCKS.BITS;
        int cz = point.getBlockZ() >> Chunk.BLOCKS.BITS;

        OutwardIterator oi = new OutwardIterator();
        oi.reset(cx, cy, cz, game.getChunkRadius() );

        while (oi.hasNext()) {
            IntVector3 v = oi.next();
            SpawnLoaderThread.addChunk(world, v.getX(), v.getY(), v.getZ());
        }

        for (int i = 0; i < 16; i++) {
            loaderThreads[i].start();
        }

        for (int i = 0; i < 16; i++) {
            try {
                loaderThreads[i].join();
            } catch (InterruptedException ie) {
                HardCoreGames.getInstance().getLogger().info("Interrupted when waiting for spawn area to load");
            }
        }

        // Add Sky to World
        world.getComponentHolder().add(NormalSky.class);

        game.setWorld(world);

    }

	public void generateNether() {

        // TODO generate Nether

	}

}
