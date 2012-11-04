package ch.minepvp.spout.hardcoregames;

import java.util.ArrayList;

import ch.minepvp.spout.hardcoregames.config.Config;
import ch.minepvp.spout.hardcoregames.config.GameDifficulty;
import ch.minepvp.spout.hardcoregames.config.GameSize;
import ch.minepvp.spout.hardcoregames.config.GameStatus;
import org.spout.api.chat.ChatArguments;
import org.spout.api.entity.Player;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;

import ch.minepvp.spout.hardcoregames.task.GenerateWorldsTask;
import org.spout.api.geo.discrete.Point;
import org.spout.api.lang.Translation;
import org.spout.api.math.IntVector3;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.OutwardIterator;
import org.spout.vanilla.component.inventory.PlayerInventory;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.material.VanillaMaterials;

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
    private ArrayList<Integer> saveSpawns;

	private GameDifficulty difficulty;


    // Difficulty Settings
    private Integer health;
    private Integer food;
    private Boolean regen;

	
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
	
	public void startGame() {

        for ( Player player : getPlayers() ) {
            player.sendMessage( ChatArguments.fromFormatString( Translation.tr("The Owner has start the Game... Worldgeneration starting...", player) ) );
        }

        calculateChunkRadius();

		GenerateWorldsTask task = new GenerateWorldsTask(this);
		
		plugin.getEngine().getScheduler().scheduleSyncDelayedTask(plugin, task, TaskPriority.HIGH);

	}


    public void calculateChunkRadius() {

        chunkRadius = 3;

    }


    public void teleportPlayersToWorld() {

        setStatus( GameStatus.RUNNING );

        // save Player and then Teleport it
        for ( Player player : getPlayers() ) {

            if ( player.isOnline() ) {

                savePlayer(player);
                setPlayerStatsForStart(player);

                teleportPlayerToRandomPosition(player);

            } else {

                getPlayers().remove(player);

            }

        }


    }

    private void setPlayerStatsForStart( Player player ) {

        // TODO set Player Stats and Items for Game Start

        // Clear Inventory
        player.add(Human.class).getInventory().clear();

        player.add(Human.class).setGamemode(GameMode.SURVIVAL);
        player.add(Human.class).getHealth().setHealth(health, player);
        // TODO set Food

    }

    private void teleportPlayerToRandomPosition( Player player ) {

        // TODO Random
        player.teleport( getWorld().getSpawnPoint().getPosition() );

    }


    private Point getSaveRandomSpawns() {

        Integer minSpawnDistance = 40; // TODO Scaled min SpawnDistance


        for ( Player player : players ) {

            Point saveSpawnPoint = null;


            while ( saveSpawnPoint != null ) {


                OutwardIterator oi = new OutwardIterator( world.getSpawnPoint().getPosition().getBlockX(),
                                                          world.getSpawnPoint().getPosition().getBlockZ(), chunkRadius * 16 );


                while ( oi.hasNext() ) {

                    if ( !checkSpawnPoint( oi.next() ) ) {
                          break;
                    }


                    if ( saveSpawnPoint != null ) {




                    } else {



                    }


                }



            }





        }

        return null;


    }

    private Boolean checkSpawnPoint( IntVector3 point ) {












        return false;

    }


    private Point getRandomPosition() {

        // TODO get a Random Position

        return null;
    }

    public void savePlayer( Player player ) {

        // Inventory
        player.add(Human.class).getData().put("hcg_inventory",player.add(Human.class).getInventory());
        player.add(Human.class).getData().put("hcg_health", player.add(Human.class).getHealth().getHealth());

        // Point
        player.add(Human.class).getData().put("hcg_point", player.getTransform().getPosition());

    }

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
        player.add(Human.class).getHealth().setHealth( (Integer)player.add(Human.class).getData().get("hcg_health"), player );
        // TODO restore food

        // Teleport back
        //player.teleport( (Point)player.add(Human.class).getData().get("hcg_point") );

        player.sendMessage("DEBUGE : DELETE");
        player.teleport(plugin.getEngine().getWorld("world").getSpawnPoint());


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
	
}
