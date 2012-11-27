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
import org.spout.vanilla.event.entity.VanillaEntityTeleportEvent;
import org.spout.vanilla.protocol.handler.player.EntityHealthChangeEvent;
import org.spout.vanilla.source.HealthChangeCause;

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

    @EventHandler
    public void onEntityHealthChangeEvent( EntityHealthChangeEvent event ) {

        if ( event.getEntity() instanceof Player) {

            Game game = gameManager.getGameByPlayer( (Player)event.getEntity() );

            if ( game != null ) {

                if ( event.getCause().equals( HealthChangeCause.REGENERATION ) ) {

                    if ( game.getRegen() == false ) {
                        event.setCancelled(true);
                    }

                }

            }

        }

    }

    @EventHandler
    public void onVanillaEntityTeleportEvent( VanillaEntityTeleportEvent event ) {

        if (  event.getFrom().getWorld().getName().startsWith("hcg_") ) {

            if ( event.getEntity() instanceof Player ) {

                Player player = (Player) event.getEntity();
                Game game = gameManager.getGameByPlayer(player);

                switch ( event.getReason() ) {
                    case PORTAL:

                        if ( event.getFrom().getWorld().equals( game.getWorld() ) ) {

                            if ( game.getPortalWorld() == null ) {
                                game.setPortalWorld( event.getFrom() );
                            }

                            if ( game.getPortalNether() == null ) {
                                event.setTo( game.getNether().getSpawnPoint().getPosition() );
                            } else {
                                event.setTo( game.getPortalNether() );
                            }

                        } else {

                            if ( game.getPortalNether() == null ) {
                                game.setPortalNether( event.getFrom() );
                            }

                            event.setTo( game.getPortalWorld() );

                        }

                    case CUSTOM:
                        game.removePlayer(player);

                }

            }

        }

    }

}