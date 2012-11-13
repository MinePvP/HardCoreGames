package ch.minepvp.spout.hardcoregames.config;

import java.io.File;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class Config extends ConfigurationHolderConfiguration{
		
    // Size
    public static final ConfigurationHolder GAME_SIZE_TINY = new ConfigurationHolder(9, "Game", "Size", "Tiny");
    public static final ConfigurationHolder GAME_SIZE_SMALL = new ConfigurationHolder(16, "Game", "Size", "Small");
    public static final ConfigurationHolder GAME_SIZE_MEDIUM = new ConfigurationHolder(25, "Game", "Size", "Medium");
    public static final ConfigurationHolder GAME_SIZE_BIG = new ConfigurationHolder(36, "Game", "Size", "Big");

    // Difficulty
    // Easy
    public static final ConfigurationHolder GAME_DIFFICULTY_EASY_HEALT_START = new ConfigurationHolder(20, "Game", "Difficulty", "Easy", "Health", "Start");
    public static final ConfigurationHolder GAME_DIFFICULTY_EASY_FOOD_START = new ConfigurationHolder(20, "Game", "Difficulty", "Easy", "Food", "Start");
    public static final ConfigurationHolder GAME_DIFFICULTY_EASY_HEALT_REGEN = new ConfigurationHolder(true, "Game", "Difficulty", "Easy", "Health", "Regen", "OverFood");

    public static final ConfigurationHolder GAME_DIFFICULTY_EASY_NOOBPROTECTION_TIME = new ConfigurationHolder(1500, "Game", "Difficulty", "Easy", "NoobProtection", "Time");

    public static final ConfigurationHolder GAME_DIFFICULTY_EASY_ARMOR_HELMET = new ConfigurationHolder(0, "Game", "Difficulty", "Easy", "Armor", "Helmet");
    public static final ConfigurationHolder GAME_DIFFICULTY_EASY_ARMOR_CHESTPLATE = new ConfigurationHolder(0, "Game", "Difficulty", "Easy", "Armor", "ChestPlate");
    public static final ConfigurationHolder GAME_DIFFICULTY_EASY_ARMOR_LEGGINGS = new ConfigurationHolder(0, "Game", "Difficulty", "Easy", "Armor", "Leggings");
    public static final ConfigurationHolder GAME_DIFFICULTY_EASY_ARMOR_BOOTS = new ConfigurationHolder(0, "Game", "Difficulty", "Easy", "Armor", "Boots");


    // Normal
    public static final ConfigurationHolder GAME_DIFFICULTY_NORMAL_HEALT_START = new ConfigurationHolder(20, "Game", "Difficulty", "Normal", "Health", "Start");
    public static final ConfigurationHolder GAME_DIFFICULTY_NORMAL_FOOD_START = new ConfigurationHolder(20, "Game", "Difficulty", "Normal", "Food", "Start");
    public static final ConfigurationHolder GAME_DIFFICULTY_NORMAL_HEALT_REGEN = new ConfigurationHolder(true, "Game", "Difficulty", "Normal", "Health", "Regen", "OverFood");

    public static final ConfigurationHolder GAME_DIFFICULTY_NORMAL_NOOBPROTECTION_TIME = new ConfigurationHolder(1200, "Game", "Difficulty", "Normal", "NoobProtection", "Time");

    public static final ConfigurationHolder GAME_DIFFICULTY_NORMAL_ARMOR_HELMET = new ConfigurationHolder(0, "Game", "Difficulty", "Normal", "Armor", "Helmet");
    public static final ConfigurationHolder GAME_DIFFICULTY_NORMAL_ARMOR_CHESTPLATE = new ConfigurationHolder(0, "Game", "Difficulty", "Normal", "Armor", "ChestPlate");
    public static final ConfigurationHolder GAME_DIFFICULTY_NORMAL_ARMOR_LEGGINGS = new ConfigurationHolder(0, "Game", "Difficulty", "Normal", "Armor", "Leggings");
    public static final ConfigurationHolder GAME_DIFFICULTY_NORMAL_ARMOR_BOOTS = new ConfigurationHolder(0, "Game", "Difficulty", "Normal", "Armor", "Boots");

    // Hard
    public static final ConfigurationHolder GAME_DIFFICULTY_HARD_HEALT_START = new ConfigurationHolder(20, "Game", "Difficulty", "Hard", "Health", "Start");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARD_FOOD_START = new ConfigurationHolder(20, "Game", "Difficulty", "Hard", "Food", "Start");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARD_HEALT_REGEN = new ConfigurationHolder(false, "Game", "Difficulty", "Hard", "Health", "Regen", "OverFood");

    public static final ConfigurationHolder GAME_DIFFICULTY_HARD_NOOBPROTECTION_TIME = new ConfigurationHolder(900, "Game", "Difficulty", "Hard", "NoobProtection", "Time");

    public static final ConfigurationHolder GAME_DIFFICULTY_HARD_ARMOR_HELMET = new ConfigurationHolder(0, "Game", "Difficulty", "Hard", "Armor", "Helmet");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARD_ARMOR_CHESTPLATE = new ConfigurationHolder(0, "Game", "Difficulty", "Hard", "Armor", "ChestPlate");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARD_ARMOR_LEGGINGS = new ConfigurationHolder(0, "Game", "Difficulty", "Hard", "Armor", "Leggings");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARD_ARMOR_BOOTS = new ConfigurationHolder(0, "Game", "Difficulty", "Hard", "Armor", "Boots");

    // Hardcore
    public static final ConfigurationHolder GAME_DIFFICULTY_HARDCORE_HEALT_START = new ConfigurationHolder(10, "Game", "Difficulty", "HardCore", "Health", "Start");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARDCORE_FOOD_START = new ConfigurationHolder(10, "Game", "Difficulty", "HardCore", "Food", "Start");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARDCORE_HEALT_REGEN = new ConfigurationHolder(false, "Game", "Difficulty", "HardCore", "Health", "Regen", "OverFood");

    public static final ConfigurationHolder GAME_DIFFICULTY_HARDCORE_NOOBPROTECTION_TIME = new ConfigurationHolder(600, "Game", "Difficulty", "HardCore", "NoobProtection", "Time");

    public static final ConfigurationHolder GAME_DIFFICULTY_HARDCORE_ARMOR_HELMET = new ConfigurationHolder(0, "Game", "Difficulty", "HardCore", "Armor", "Helmet");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARDCORE_ARMOR_CHESTPLATE = new ConfigurationHolder(0, "Game", "Difficulty", "HardCore", "Armor", "ChestPlate");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARDCORE_ARMOR_LEGGINGS = new ConfigurationHolder(0, "Game", "Difficulty", "HardCore", "Armor", "Leggings");
    public static final ConfigurationHolder GAME_DIFFICULTY_HARDCORE_ARMOR_BOOTS = new ConfigurationHolder(0, "Game", "Difficulty", "HardCore", "Armor", "Boots");

    public Config(File dataFolder) {
            super(new YamlConfiguration(new File(dataFolder, "config.yml")));
    }

    @Override
    public void load() throws ConfigurationException {
            super.load();
            super.save();
    }


    private void loadItems() {










    }
	
}
