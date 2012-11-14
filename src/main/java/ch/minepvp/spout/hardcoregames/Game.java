package ch.minepvp.spout.hardcoregames;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import ch.minepvp.spout.hardcoregames.config.Config;
import ch.minepvp.spout.hardcoregames.config.GameDifficulty;
import ch.minepvp.spout.hardcoregames.config.GameSize;
import ch.minepvp.spout.hardcoregames.config.GameStatus;
import ch.minepvp.spout.hardcoregames.task.NoobProtectionTask;
import org.spout.api.chat.ChatArguments;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;

import ch.minepvp.spout.hardcoregames.task.GenerateWorldsTask;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.lang.Translation;
import org.spout.api.scheduler.TaskPriority;
import org.spout.vanilla.component.inventory.PlayerInventory;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.component.misc.HealthComponent;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.material.VanillaMaterials;
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

    private ItemStack armorHelmet = null;
    private ItemStack armorChestPlate = null;
    private ItemStack armorLeggings = null;
    private ItemStack armorBoots = null;

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
        loadArmorSettings();
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

    private void loadArmorSettings() {

        if ( difficulty.equals(GameDifficulty.EASY) ) {

            if ( Config.GAME_DIFFICULTY_EASY_ARMOR_HELMET.getShort() > 0 ) {
                armorHelmet = new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_EASY_ARMOR_HELMET.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_EASY_ARMOR_CHESTPLATE.getShort() > 0 ) {
                armorChestPlate = new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_EASY_ARMOR_CHESTPLATE.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_EASY_ARMOR_LEGGINGS.getShort() > 0 ) {
                armorLeggings =  new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_EASY_ARMOR_LEGGINGS.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_EASY_ARMOR_BOOTS.getShort() > 0 ) {
                armorBoots =  new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_EASY_ARMOR_BOOTS.getShort() ), 1 );
            }

        } else if ( difficulty.equals(GameDifficulty.NORMAL) ) {

            if ( Config.GAME_DIFFICULTY_NORMAL_ARMOR_HELMET.getShort() > 0 ) {
                armorHelmet = new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_NORMAL_ARMOR_HELMET.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_NORMAL_ARMOR_CHESTPLATE.getShort() > 0 ) {
                armorChestPlate = new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_NORMAL_ARMOR_CHESTPLATE.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_NORMAL_ARMOR_LEGGINGS.getShort() > 0 ) {
                armorLeggings =  new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_NORMAL_ARMOR_LEGGINGS.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_NORMAL_ARMOR_BOOTS.getShort() > 0 ) {
                armorBoots =  new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_NORMAL_ARMOR_BOOTS.getShort() ), 1 );
            }

        } else if ( difficulty.equals(GameDifficulty.HARD) ) {

            if ( Config.GAME_DIFFICULTY_HARD_ARMOR_HELMET.getShort() > 0 ) {
                armorHelmet = new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_HARD_ARMOR_HELMET.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_HARD_ARMOR_CHESTPLATE.getShort() > 0 ) {
                armorChestPlate = new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_HARD_ARMOR_CHESTPLATE.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_HARD_ARMOR_LEGGINGS.getShort() > 0 ) {
                armorLeggings =  new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_HARD_ARMOR_LEGGINGS.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_HARD_ARMOR_BOOTS.getShort() > 0 ) {
                armorBoots =  new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_HARD_ARMOR_BOOTS.getShort() ), 1 );
            }

        } else if ( difficulty.equals(GameDifficulty.HARDCORE) ) {

            if ( Config.GAME_DIFFICULTY_HARDCORE_ARMOR_HELMET.getShort() > 0 ) {
                armorHelmet = new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_HARDCORE_ARMOR_HELMET.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_HARDCORE_ARMOR_CHESTPLATE.getShort() > 0 ) {
                armorChestPlate = new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_HARDCORE_ARMOR_CHESTPLATE.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_HARDCORE_ARMOR_LEGGINGS.getShort() > 0 ) {
                armorLeggings =  new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_HARDCORE_ARMOR_LEGGINGS.getShort() ), 1 );
            }

            if ( Config.GAME_DIFFICULTY_HARDCORE_ARMOR_BOOTS.getShort() > 0 ) {
                armorBoots =  new ItemStack( VanillaMaterials.getMaterial( Config.GAME_DIFFICULTY_HARDCORE_ARMOR_BOOTS.getShort() ), 1 );
            }

        }

    }

    /**
     *
     */
	public void startGame() {

        for ( Player player : getPlayers() ) {
            player.sendMessage( ChatArguments.fromFormatString( Translation.tr("The Owner has start the Game... Worldgeneration starting...", player) ) );
        }

        calculateChunkRadius();

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

        if ( chunkRadius <= 5 ) {
            chunkRadius = 6;
        }

    }

    /**
     * Calculate the ege Points of the Battlefield
     */
    public void calculateP1AndP2() {

        // Point 1
        int x = (getWorld().getSpawnPoint().getPosition().getChunkX() - getChunkRadius()) * 16;
        int z = (getWorld().getSpawnPoint().getPosition().getChunkZ() - getChunkRadius()) * 16;
        p1 = new Point(getWorld(), x,0,z);

        // Point 2
        x = (getWorld().getSpawnPoint().getPosition().getChunkX() + getChunkRadius()) * 16;
        z = (getWorld().getSpawnPoint().getPosition().getChunkZ() + getChunkRadius()) * 16;
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

                getWorld().getChunkFromBlock(saveSpawns.get(i));
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

        // Clear Inventory
        player.add(PlayerInventory.class).clear();

        player.add(Human.class).setGamemode(GameMode.SURVIVAL);

        // Set Health and Food
        player.add(HealthComponent.class).setHealth(health, HealthChangeCause.SPAWN);
        // TODO set Food


        // TODO set Items


        // Set Armor
        if ( armorHelmet != null ) {
            player.add(PlayerInventory.class).getArmor().setHelmet(armorHelmet);
        }

        if ( armorChestPlate != null ) {
            player.add(PlayerInventory.class).getArmor().setChestPlate(armorChestPlate);
        }

        if ( armorLeggings != null ) {
            player.add(PlayerInventory.class).getArmor().setLeggings(armorLeggings);
        }

        if ( armorBoots != null ) {
            player.add(PlayerInventory.class).getArmor().setBoots(armorBoots);
        }

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

                    plugin.getLogger().info("Fount Spawn Point for " + player.getName() + " X : " + point.getBlockX() + " Y : " + point.getBlockY() + " Z : " + point.getBlockZ());

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
        player.add(Human.class).getData().put("hcg_inventory",player.add(PlayerInventory.class).getClass());

        // Health
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
        player.add(PlayerInventory.class).getArmor().clear();
        player.add(PlayerInventory.class).getArmor().setBoots( inventory.getArmor().getBoots() );
        player.add(PlayerInventory.class).getArmor().setLeggings(inventory.getArmor().getLeggings());
        player.add(PlayerInventory.class).getArmor().setChestPlate(inventory.getArmor().getChestPlate());
        player.add(PlayerInventory.class).getArmor().setHelmet(inventory.getArmor().getHelmet());


        // Main
        player.add(PlayerInventory.class).getMain().clear();

        for ( int i = 0; i > inventory.getMain().size(); i++ ) {
            player.add(PlayerInventory.class).getMain().set(i, inventory.getMain().get(i));
        }

        // Quickbar
        player.add(PlayerInventory.class).getQuickbar().clear();

        for ( int i = 0; i > inventory.getQuickbar().size(); i++ ) {
            player.add(PlayerInventory.class).getQuickbar().set(i, inventory.getQuickbar().get(i));
        }

        // Restore Healt
        player.add(HealthComponent.class).setHealth( (Integer)player.add(Human.class).getData().get("hcg_health"), HealthChangeCause.SPAWN );
        // TODO restore food

        // Teleport back
        player.teleport( (Point)player.add(Human.class).getData().get("hcg_point") );

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

            if ( getPlayers().size() > 0 ) {

                player.sendMessage( ChatArguments.fromFormatString( Translation.tr("{{RED}}You have loos the Game!", player) ) );

                for ( Player toPlayer : getPlayers() ) {
                    player.sendMessage( ChatArguments.fromFormatString( Translation.tr("{{GOLD}}[Game] {{RED}}%1 {{GOLD}}has died! {{RED}}%2 {{GOLD}}left...", player, player.getName(), getPlayers().size()) ) );
                }

            } else {

                Player winner = getPlayers().get(0);
                winner.sendMessage( ChatArguments.fromFormatString( Translation.tr("{{GOLD}}You have won the Game!", winner) ) );
                restorePlayer(winner);
            }

            restorePlayer( player );
        } else {

            player.sendMessage( ChatArguments.fromFormatString( Translation.tr("{{RED}}You leave the Game!", player)) );

            if ( player.equals( getOwner() ) ) {

                if ( getPlayers().size() > 0 ) {
                    setOwner( getPlayers().get(0) );
                }

            }

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

    public ItemStack getArmorBoots() {
        return armorBoots;
    }

    public void setArmorBoots(ItemStack armorBoots) {
        this.armorBoots = armorBoots;
    }

    public ItemStack getArmorHelmet() {
        return armorHelmet;
    }

    public void setArmorHelmet(ItemStack armorHelmet) {
        this.armorHelmet = armorHelmet;
    }

    public ItemStack getArmorChestPlate() {
        return armorChestPlate;
    }

    public void setArmorChestPlate(ItemStack armorChestPlate) {
        this.armorChestPlate = armorChestPlate;
    }

    public ItemStack getArmorLeggings() {
        return armorLeggings;
    }

    public void setArmorLeggings(ItemStack armorLeggings) {
        this.armorLeggings = armorLeggings;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getFood() {
        return food;
    }

    public void setFood(Integer food) {
        this.food = food;
    }

    public Boolean getRegen() {
        return regen;
    }

    public void setRegen(Boolean regen) {
        this.regen = regen;
    }
}
