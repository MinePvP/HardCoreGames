package ch.minepvp.spout.hardcoregames.task;

import ch.minepvp.spout.hardcoregames.Game;
import org.spout.api.chat.ChatArguments;
import org.spout.api.entity.Player;
import org.spout.api.lang.Translation;

public class NoobProtectionTask implements Runnable{

    private Game game;

    public NoobProtectionTask( Game game ) {
        this.game = game;
    }

    @Override
    public void run() {
        game.setNoobProtection(false);

        for ( Player player : game.getPlayers() ) {

            if ( player.isOnline() ) {

                player.sendMessage(ChatArguments.fromFormatString(Translation.tr("{{YELLOW}}Noob Protection is now Off!!!", player) ) );

            }


        }

    }

}
