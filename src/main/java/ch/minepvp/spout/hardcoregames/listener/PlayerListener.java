package ch.minepvp.spout.hardcoregames.listener;

import ch.minepvp.spout.hardcoregames.Game;
import ch.minepvp.spout.hardcoregames.config.GameStatus;
import ch.minepvp.spout.hardcoregames.manager.GameManager;
import org.spout.api.chat.ChatArguments;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerLeaveEvent;
import org.spout.api.lang.Translation;
import org.spout.vanilla.event.player.PlayerDeathEvent;

import ch.minepvp.spout.hardcoregames.HardCoreGames;
import org.spout.vanilla.event.player.network.PlayerListEvent;
import org.spout.vanilla.protocol.handler.player.EntityHealthChangeEvent;
import org.spout.vanilla.source.HealthChangeCause;

public class PlayerListener implements Listener {
	
	private HardCoreGames plugin;
    private GameManager gameManager;

	
	public PlayerListener() {
		plugin = HardCoreGames.getInstance();
        gameManager = plugin.getGameManager();
	}
	
	@EventHandler
    public void onPlayerChat( PlayerChatEvent event ) {
		
        Player player = event.getPlayer();

        Game game = gameManager.getGameByPlayer(player);

        if ( game != null ) {

            if ( game.getStatus().equals( GameStatus.RUNNING ) ) {

                for ( Player toPlayer : game.getPlayers() ) {

                    player.sendMessage(ChatArguments.fromFormatString(Translation.tr("[{{GOLD}}Game{{WHITE}}] %0 : %1", player, player.getName(), event.getMessage().getPlainString())));

                }

            }

        }
		
	}

    @EventHandler
    public void onPlayerDeathEvent( PlayerDeathEvent event ) {

        Player player = event.getPlayer();

        Game game = gameManager.getGameByPlayer(player);

        if ( game != null ) {

            game.removePlayer(player);

            if ( game.getPlayers().size() == 0 ) {
                gameManager.removeGame(game);
            }

        }

    }

    @EventHandler void onPlayerLeaveEvent( PlayerLeaveEvent event ) {

        Player player = event.getPlayer();

        Game game = gameManager.getGameByPlayer(player);

        if ( game != null ) {

            game.removePlayer(player);

            if ( game.getPlayers().size() == 0 ) {
                gameManager.removeGame(game);
            }

        }

    }

    @EventHandler
    public void onPlayerFoodSaturationChangeEvent( EntityHealthChangeEvent event ) {

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

}
