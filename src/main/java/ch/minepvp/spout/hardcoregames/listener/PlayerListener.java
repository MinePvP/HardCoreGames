package ch.minepvp.spout.hardcoregames.listener;

import ch.minepvp.spout.hardcoregames.Game;
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
import org.spout.vanilla.material.VanillaMaterials;

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

            for ( Player toPlayer : game.getPlayers() ) {

                player.sendMessage(ChatArguments.fromFormatString(Translation.tr("[Game] %1 : %2", player, player.getName(), event.getMessage().getPlainString())));

            }

        }
		
	}

    @EventHandler
    public void onPlayerDeathEvent( PlayerDeathEvent event ) {

        Player player = event.getPlayer();

        // TODO DELETE
        player.sendMessage("DEBUG: YOU ARE DEAD");


        Game game = gameManager.getGameByPlayer(player);

        if ( game != null ) {

            game.removePlayer( player );
            game.restorePlayer(player);

            if ( game.getPlayers().size() > 1 ) {

                for ( Player toPlayer : game.getPlayers() ) {

                    player.sendMessage( ChatArguments.fromFormatString( Translation.tr("[Game] %1 has died! %2 left...", player, player.getName(), game.getPlayers().size() ) ) );

                }

                for ( Player toPlayer : game.getPlayers() ) {

                    toPlayer.sendMessage( ChatArguments.fromFormatString( Translation.tr("%1 has died! %2 left...", toPlayer, player.getName(), game.getPlayers().size()) ) );

                }

            } else {

                Player winner = game.getPlayers().get(0);

                winner.sendMessage( ChatArguments.fromFormatString( Translation.tr("You have won the Game!", winner) ) );

                game.restorePlayer(winner);
                gameManager.removeGame(game);

            }

        }

    }

    @EventHandler
    public void onPlayerInteractEvent( PlayerInteractEvent event ) {


        // TODO remove this testing things
        if ( event.getHeldItem() != null ) {
            if ( event.getHeldItem().getMaterial().equals(VanillaMaterials.WOODEN_AXE) ) {

                Point point = event.getInteractedPoint();

                Block block = event.getPlayer().getWorld().getBlock(point, event.getPlayer());
                block.setMaterial(VanillaMaterials.BEDROCK);

            }
        }


    }

	
}
