package ch.minepvp.spout.hardcoregames.listener;

import ch.minepvp.spout.hardcoregames.manager.GameManager;
import ch.minepvp.spout.hardcoregames.HardCoreGames;
import org.spout.api.event.Listener;

public class EntityListener implements Listener {

    private HardCoreGames plugin;
    private GameManager gameManager;


    public EntityListener() {
        plugin = HardCoreGames.getInstance();
        gameManager = plugin.getGameManager();
    }


}
