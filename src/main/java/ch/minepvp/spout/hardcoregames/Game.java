package ch.minepvp.spout.hardcoregames;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import ch.minepvp.spout.hardcoregames.config.Config;
import ch.minepvp.spout.hardcoregames.config.GameDifficulty;
import ch.minepvp.spout.hardcoregames.config.GameSize;
import ch.minepvp.spout.hardcoregames.config.GameStatus;
import ch.minepvp.spout.hardcoregames.task.NoobProtectionTask;
import com.sun.jdi.IntegerType;
import org.spout.api.chat.ChatArguments;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;

import ch.minepvp.spout.hardcoregames.task.GenerateWorldsTask;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.lang.Translation;
import org.spout.api.math.IntVector3;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.OutwardIterator;
import org.spout.vanilla.component.inventory.PlayerInventory;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.component.misc.HealthComponent;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.source.HealthChangeCause;

public class Game {
	
	private HardCoreGames plugin;
	
	private GameStatus status;
	
	private Player owner;
	private ArrayList<Player> players;
	
	private World world;
	private World nether;
	
	private GameSize size;
    private Integer sizeInt;
    private Integer chunkRadius;
    private Point p1;
    private Point p2;

    private Integer minSpawnDistance = 40;
    private ArrayList<Point> saveSpawns;

	private GameDifficulty difficulty;


    // Difficulty Settings
    private Integer health;
    private Integer food;
    private Boolean regen;

    private Boolean noobProtection = true;

	
	public Game ( Player owner, GameDifficulty difficutly, GameSize size ) {
		
		plugin = HardCoreGames.getInstance();

        players = new ArrayList<Player>();

		setOwner(owner);
        addPlayer(owner);
        setDifficulty(difficutly);
		setSize(size);

        setStatus( GameStatus.NEW );

        loadSizeSettings();
        loadDifficultySettings();
	}

    private void loadSizeSettings() {

        if ( size.equals( GameSize.TINY ) ) {
            sizeInt = Config.GAME_SIZE_TINY.getInt();

        } else if ( size.equals( GameSize.SMALL ) ) {
            sizeInt = Config.GAME_SIZE_SMALL.getInt();

        } else if ( size.equals( GameSize.MEDIUM ) ) {
            sizeInt = Config.GAME_SIZE_MEDIUM.getInt();

        } else if ( size.equals( GameSize.BIG ) ) {
            sizeInt = Config.GAME_SIZE_BIG.getInt();
        }

    }

    private void loadDifficultySettings() {

        if ( difficulty.equals(GameDifficulty.EASY) ) {

            health = Config.GAME_DIFFICULTY_EASY_HEALT_START.getInt();
            food = Config.GAME_DIFFICULTY_EASY_FOOD_START.getInt();
            regen = Config.GAME_DIFFICULTY_EASY_HEALT_REGEN.getBoolean();


        } else if ( difficulty.equals(GameDifficulty.NORMAL) ) {

            health = Config.GAME_DIFFICULTY_NORMAL_HEALT_START.getInt();
            food = Config.GAME_DIFFICULTY_NORMAL_FOOD_START.getInt();
            regen = Config.GAME_DIFFICULTY_NORMAL_HEALT_REGEN.getBoolean();

        } else if ( difficulty.equals(GameDifficulty.HARD) ) {

            health = Config.GAME_DIFFICULTY_HARD_HEALT_START.getInt();
            food = Config.GAME_DIFFICULTY_HARD_FOOD_START.getInt();
            regen = Config.GAME_DIFFICULTY_HARD_HEALT_REGEN.getBoolean();

        } else if ( difficulty.equals(GameDifficulty.HARDCORE) ) {

            health = Config.GAME_DIFFICULTY_HARDCORE_HEALT_START.getInt();
            food = Config.GAME_DIFFICULTY_HARDCORE_FOOD_START.getInt();
            regen = Config.GAME_DIFFICULTY_HARDCORE_HEALT_REGEN.getBoolean();

        }


    }

    private void loadChunkRadius() {

        chunkRadius = players.size() * sizeInt;

    }

    /**
     *
     */
	public void startGame() {

        for ( Player player : getPlayers() ) {
            player.sendMessage( ChatArguments.fromFormatString( Translation.tr("The Owner has start the Game... Worldgeneration starting...", player) ) );
        }

        calculateChunkRadius();
        calculateP1AndP2();

		GenerateWorldsTask task1 = new GenerateWorldsTask(this);
		plugin.getEngine().getScheduler().scheduleSyncDelayedTask(plugin, task1, TaskPriority.HIGH);

        NoobProtectionTask task2 = new NoobProtectionTask(this);
        plugin.getEngine().getScheduler().scheduleAsyncDelayedTask(plugin, task2, getNoobProtectionTime(), TaskPriority.LOW);


    }

    /**
     * Load the Noobprotection Time
     *
     * @return
     */
    private Long getNoobProtectionTime() {

        Long intervall = null;

        if ( difficulty.equals(GameDifficulty.EASY) ) {
            intervall = Config.GAME_DIFFICULTY_EASY_NOOBPROTECTION_TIME.getLong();

        } else if ( difficulty.equals(GameDifficulty.NORMAL) ) {
            intervall = Config.GAME_DIFFICULTY_NORMAL_NOOBPROTECTION_TIME.getLong();

        } else if ( difficulty.equals(GameDifficulty.HARD) ) {
            intervall = Config.GAME_DIFFICULTY_HARD_NOOBPROTECTION_TIME.getLong();

        } else if ( difficulty.equals(GameDifficulty.HARDCORE) ) {
            intervall = Config.GAME_DIFFICULTY_HARDCORE_NOOBPROTECTION_TIME.getLong();

        }

        return ( intervall * 20 ) * 60;
    }

    /**
     * Calculate the Chunk Radius for the Game over the Player Size
     */
    public void calculateChunkRadius() {

        int chunks = (players.size() * sizeInt);

        // only odd numbers
        for ( int i = 1; i < 300; i = i + 2 ) {

            if ( ( i * i ) >= chunks) {
                chunkRadius = (( i - 1 ) / 2) + 1;
                i = 301;

            }

        }

        if ( chunkRadius < 4 ) {
            chunkRadius = 5;
        }

    }

    /**
     * Calculate the ege Points of the Battlefield
     */
    private void calculateP1AndP2() {

        int x = getWorld().getSpawnPoint().getPosition().getChunkX() - getChunkRadius();
        int z = getWorld().getSpawnPoint().getPosition().getChunkZ() - getChunkRadius();

        p1 = new Point(getWorld(), x,0,z);


        x = getWorld().getSpawnPoint().getPosition().getChunkX() + getChunkRadius();
        z = getWorld().getSpawnPoint().getPosition().getChunkZ() + getChunkRadius();

        p2 = new Point(getWorld(), x,0,z);

    }

    /**
     * Teleport the Players to her Save Random Spawn Points
     */
    public void teleportPlayersToWorld() {

        int i = 0;

        // save Player and then Teleport it
        for ( Player player : getPlayers() ) {

            if ( player.isOnline() ) {

                savePlayer(player);
                setPlayerStatsForStart(player);

                saveSpawns.get(i).getWorld().getChunkFromBlock(saveSpawns.get(i));
                player.teleport( saveSpawns.get(i) );

                i++;

            } else {

                getPlayers().remove(player);

            }

        }

    }

    /**
     * Get a Random Integer between min. and max.
     *
     * @param min
     * @param max
     * @return
     */
    private Integer randomBetween( Integer min, Integer max ) {

        SecureRandom secRandom = null;

        try {
            secRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return secRandom.nextInt( max - min + 1 )+min;
    }

    /**
     * Prepare the Player for Starting the Game
     *
     * @param player
     */
    private void setPlayerStatsForStart( Player player ) {

        // TODO set Player Stats and Items for Game Start

        // Clear Inventory
        player.add(Human.class).getInventory().clear();

        player.add(Human.class).setGamemode(GameMode.SURVIVAL);

        // Set Health and Food
        player.add(HealthComponent.class).setHealth(health, HealthChangeCause.SPAWN);
        // TODO set Food



        // TODO set Items



        // TODO set Armor
        //player.add(Human.class).getInventory().getArmor().setHelmet();
        //player.add(Human.class).getInventory().getArmor().setChestPlate();
        //player.add(Human.class).getInventory().getArmor().setLeggings();
        //player.add(Human.class).getInventory().getArmor().setBoots();
    }

    /**
     * Search Spawns for the Players
     *
     * @return
     */
    public void getRandomSpawns() {

        saveSpawns = new ArrayList<Point>();

        for ( Player player : players ) {

            Point saveSpawnPoint = null;

            while ( saveSpawnPoint == null ) {

                int x = randomBetween( p1.getBlockX(), p2.getBlockX() );
                int z = randomBetween( p1.getBlockZ(), p2.getBlockZ() );

                Point point = new Point(world, x, 1, z);

                if ( checkSpawnPoint( point ) ) {

                    plugin.getLogger().info("Fount Spawn Point for Player X : " + point.getBlockX() + " Y : " + point.getBlockY() + " Z : " + point.getBlockZ());

                    saveSpawnPoint = point;
                    saveSpawns.add(point);

                }

            }

        }

    }

    /**
     * Check the SpawnPoint for Safety
     *
     * @param point
     * @return
     */
    private Boolean checkSpawnPoint( Point point ) {


        if ( saveSpawns.size() == 0 ) {
            return true;
        }

        for ( Point point2 : saveSpawns ) {

            if ( point2.distance(point) >= minSpawnDistance ) {

                Block block = world.getBlock(point);

                if ( block.getMaterial() instanceof Solid ) {
                    return true;
                }

            }

        }

        return false;

    }

    /**
     * Save all Player Data before a Game start
     *
     * @param player
     */
    public void savePlayer( Player player ) {

        // Inventory
        player.add(Human.class).getData().put("hcg_inventory",player.add(Human.class).getInventory());
        player.add(Human.class).getData().put("hcg_health", player.add(Human.class).getHealth().getHealth());

        // Point
        player.add(Human.class).getData().put("hcg_point", player.getTransform().getPosition());

    }

    /**
     * Restore the Player
     *
     * @param player
     */
    public void restorePlayer( Player player ) {

        // Restore Inventory
        PlayerInventory inventory = (PlayerInventory) player.add(Human.class).getData().get("hcg_inventory");

        // Armor
        player.add(Human.class).getInventory().getArmor().clear();
        player.add(Human.class).getInventory().getArmor().setBoots( inventory.getArmor().getBoots() );
        player.add(Human.class).getInventory().getArmor().setLeggings(inventory.getArmor().getLeggings());
        player.add(Human.class).getInventory().getArmor().setChestPlate(inventory.getArmor().getChestPlate());
        player.add(Human.class).getInventory().getArmor().setHelmet(inventory.getArmor().getHelmet());


        // Main
        player.add(Human.class).getInventory().getMain().clear();

        for ( int i = 0; i > inventory.getMain().size(); i++ ) {
            player.add(Human.class).getInventory().getMain().set(i, inventory.getMain().get(i));
        }

        // Quickbar
        player.add(Human.class).getInventory().getQuickbar().clear();

        for ( int i = 0; i > inventory.getQuickbar().size(); i++ ) {
            player.add(Human.class).getInventory().getQuickbar().set(i, inventory.getQuickbar().get(i));
        }

        // Restore Healt
        player.add(HealthComponent.class).setHealth( (Integer)player.add(Human.class).getData().get("hcg_health"), HealthChangeCause.SPAWN );
        // TODO restore food

        // Teleport back
        player.teleport( (Point)player.add(Human.class).getData().get("hcg_point") );

        //player.teleport(plugin.getEngine().getWorld("world").getSpawnPoint());


    }
	
	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer( Player player ) {
		this.players.add(player);
	}

    public void removePlayer( Player player ) {
        this.players.remove( player );

        if ( getStatus().equals( GameStatus.RUNNING) ) {
            restorePlayer( player );
        }

    }

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getNether() {
		return nether;
	}

	public void setNether(World nether) {
		this.nether = nether;
	}

	public GameSize getSize() {
		return size;
	}

	public void setSize(GameSize size) {
		this.size = size;
	}

    public Integer getSizeInt() {
        return sizeInt;
    }

    public void setSizeInt(Integer size) {
        this.sizeInt = size;
    }

    public Integer getChunkRadius() {
        return chunkRadius;
    }

    public void setChunkRadius(Integer chunkRadius) {
        this.chunkRadius = chunkRadius;
    }

	public GameDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(GameDifficulty difficulty) {
		this.difficulty = difficulty;
	}

    public Boolean getNoobProtection() {
        return noobProtection;
    }

    public void setNoobProtection(Boolean noobProtection) {
        this.noobProtection = noobProtection;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }
}
