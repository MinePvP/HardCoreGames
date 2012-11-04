package ch.minepvp.spout.hardcoregames.manager;

import ch.minepvp.spout.hardcoregames.Game;
import ch.minepvp.spout.hardcoregames.HardCoreGames;
import org.spout.api.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class GameManager {

    private HardCoreGames plugin;

    private ArrayList<Game> games;

    public GameManager() {
        plugin = HardCoreGames.getInstance();

        // Games
        games = new ArrayList<Game>();
    }



    public void addGame( Game game ) {
        games.add(game);
    }

    public void removeAllGames() {

        if ( games.size() > 0 ) {

            for ( Game game : games ) {
                removeGame(game);
            }

        }

    }

    public void removeGame( Game game ) {

        // Unload and Delete World
        plugin.getEngine().unloadWorld(game.getWorld(), true);

        for ( File file : plugin.getEngine().getWorldFolders() ) {

            if ( file.getName().equalsIgnoreCase( game.getWorld().getName() ) ) {

                deleteFolder(file);

            }

        }

        games.remove(game);
    }

    public boolean deleteFolder(File file) {

        if (file.exists()) {

            if (file.isDirectory()) {

                for (File f : file.listFiles()) {

                    if (!deleteFolder(f)) {
                        return false;
                    }
                }
            }

            file.delete();
            return !file.exists();

        } else {
            return false;
        }

    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public Game getGameByPlayer( Player player ) {

        for ( Game game : games ) {

            for ( Player player2 : game.getPlayers() ) {

                if ( player.getName().equalsIgnoreCase( player2.getName() ) ) {
                    return game;
                }

            }

        }

        return null;

    }

}
