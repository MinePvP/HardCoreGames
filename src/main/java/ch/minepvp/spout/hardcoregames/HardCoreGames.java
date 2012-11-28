package ch.minepvp.spout.hardcoregames;

import ch.minepvp.spout.hardcoregames.commands.HardCoreGamesCommand;
import ch.minepvp.spout.hardcoregames.config.Config;
import ch.minepvp.spout.hardcoregames.listener.BlockListener;
import ch.minepvp.spout.hardcoregames.listener.EntityListener;
import ch.minepvp.spout.hardcoregames.listener.PlayerListener;
import ch.minepvp.spout.hardcoregames.manager.GameManager;
import org.spout.api.UnsafeMethod;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.plugin.CommonPlugin;

import java.util.logging.Level;

public class HardCoreGames extends CommonPlugin {
	
	private static HardCoreGames instance;
	
	private Config config;

    private GameManager gameManager;
	
	@Override
	@UnsafeMethod
	public void onLoad() {
		
		instance = this;
		
		// Initial Config
        config = new Config( getDataFolder() );

        getLogger().info("Loaded");
	}
	
	@Override
	@UnsafeMethod
	public void onEnable() {
		
		// Load Config
        try {
            config.load();
        } catch (ConfigurationException e) {
            getLogger().log(Level.WARNING, "Error loading HardCoreGames configuration: ", e);
        }

        gameManager = new GameManager();

        // Register Listener
        getEngine().getEventManager().registerEvents(new PlayerListener(), this);
        getEngine().getEventManager().registerEvents(new BlockListener(), this);
        getEngine().getEventManager().registerEvents(new EntityListener(), this);


        //Register commands
        CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());
        getEngine().getRootCommand().addSubCommands(this, HardCoreGamesCommand.class, commandRegFactory);


        getLogger().info("Enabled");
	}
	
	@Override
	@UnsafeMethod
	public void onDisable() {
		
		gameManager.removeAllGames();

		getLogger().info("Disabled");
	}
	
	public static HardCoreGames getInstance() {
		return instance;
	}

    public Config getConfig() {
        return config;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

}
