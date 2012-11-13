package ch.minepvp.spout.hardcoregames.listener;

import ch.minepvp.spout.hardcoregames.Game;
import ch.minepvp.spout.hardcoregames.manager.GameManager;
import ch.minepvp.spout.hardcoregames.HardCoreGames;
import org.spout.api.chat.ChatArguments;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.lang.Translation;
import org.spout.vanilla.event.entity.EntityDamageEvent;

public class EntityListener implements Listener {

    private HardCoreGames plugin;
    private GameManager gameManager;


    public EntityListener() {
        plugin = HardCoreGames.getInstance();
        gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void onPlayerDeathEvent( EntityDamageEvent event ) {

        if ( event.getEntity() instanceof Player ) {

            if ( event.getDamager() instanceof Player ) {

                Player damager = (Player)event.getDamager();
                Game game = gameManager.getGameByPlayer( damager );

                if ( game != null ) {

                    if ( game.getNoobProtection() ) {
                        event.setCancelled(true);
                        damager.sendMessage(ChatArguments.fromFormatString(Translation.tr("{{RED}}Noob Protection is still on!", damager) ) );
                    }

                }

            }

        }

    }


}