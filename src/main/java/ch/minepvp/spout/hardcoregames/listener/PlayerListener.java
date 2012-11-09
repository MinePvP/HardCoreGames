package ch.minepvp.spout.hardcoregames.listener;

import ch.minepvp.spout.hardcoregames.Game;
import ch.minepvp.spout.hardcoregames.config.GameStatus;
import ch.minepvp.spout.hardcoregames.manager.GameManager;
import org.spout.api.chat.ChatArguments;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.lang.Translation;
import org.spout.vanilla.event.player.PlayerDeathEvent;

import ch.minepvp.spout.hardcoregames.HardCoreGames;
import org.spout.vanilla.event.player.PlayerFoodSaturationChangeEvent;
import org.spout.vanilla.material.VanillaMaterials;
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

                    player.sendMessage(ChatArguments.fromFormatString(Translation.tr("[{{GOLD}}Game{{WHITE}}] %1 : %2", player, player.getName(), event.getMessage().getPlainString())));

                }

            }

        }
		
	}

    @EventHandler
    public void onPlayerDeathEvent( PlayerDeathEvent event ) {

        Player player = event.getPlayer();

        Game game = gameManager.getGameByPlayer(player);

        if ( game != null ) {

            game.removePlayer( player );
            game.restorePlayer(player);
            player.sendMessage( ChatArguments.fromFormatString( Translation.tr("{{RED}}You have loos the Game!", player) ) );

            if ( game.getPlayers().size() > 1 ) {

                for ( Player toPlayer : game.getPlayers() ) {

                    player.sendMessage( ChatArguments.fromFormatString( Translation.tr("{{GOLD}}[Game] {{RED}}%1 {{GOLD}}has died! {{RED}}%2 {{GOLD}}left...", player, player.getName(), game.getPlayers().size() ) ) );

                }

                for ( Player toPlayer : game.getPlayers() ) {

                    toPlayer.sendMessage( ChatArguments.fromFormatString( Translation.tr("{{RED}}%1 {{GOLD}}has died! {{RED}}%2 {{GOLD}}left...", toPlayer, player.getName(), game.getPlayers().size()) ) );

                }

            } else {

                Player winner = game.getPlayers().get(0);

                winner.sendMessage( ChatArguments.fromFormatString( Translation.tr("{{GOLD}}You have won the Game!", winner) ) );

                game.restorePlayer(winner);
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

                    event.setCancelled(true);

                }

            }

        }

    }

	
}
