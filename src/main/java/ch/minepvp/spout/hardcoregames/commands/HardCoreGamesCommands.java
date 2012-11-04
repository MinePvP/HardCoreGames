package ch.minepvp.spout.hardcoregames.commands;

import ch.minepvp.spout.hardcoregames.Game;
import ch.minepvp.spout.hardcoregames.config.GameDifficulty;
import ch.minepvp.spout.hardcoregames.config.GameSize;
import ch.minepvp.spout.hardcoregames.manager.GameManager;
import ch.minepvp.spout.hardcoregames.HardCoreGames;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.lang.Translation;

public class HardCoreGamesCommands {

    private final HardCoreGames plugin;
    private GameManager gameManager;

    public HardCoreGamesCommands( HardCoreGames instance ) {
        plugin = instance;
        gameManager = plugin.getGameManager();
    }

    @Command(aliases = {"help"}, usage = "", desc = "List all Messages.")
    @CommandPermissions("hcg.help")
    public void help(CommandContext args, CommandSource source) throws CommandException {

        source.sendMessage( ChatStyle.DARK_GREEN, "-----------------------------------------------------" );
        source.sendMessage( ChatStyle.DARK_GREEN, "Help" );
        source.sendMessage( ChatStyle.DARK_GREEN, "-----------------------------------------------------" );

        if ( source.hasPermission("hcg.create") ) {
            source.sendMessage( ChatStyle.BLUE, "/hcg create <easy|normal|hard|hardcore> <tiny|small|medium|big>" );
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("Create a new HardCore Game", source)) );
        }

        if ( source.hasPermission("hcg.list") ) {
            source.sendMessage( ChatStyle.BLUE, "/hcg list" );
            source.sendMessage( ChatArguments.fromFormatString( Translation.tr("List all running Games", source) ) );
        }

        if ( source.hasPermission("hcg.join") ) {
            source.sendMessage( ChatStyle.BLUE, "/hcg join <player>" );
            source.sendMessage( ChatArguments.fromFormatString( Translation.tr("Join a Game over a Player", source) ) );
        }

        if ( source.hasPermission("hcg.leave") ) {
            source.sendMessage( ChatStyle.BLUE, "/hcg leave" );
            source.sendMessage( ChatArguments.fromFormatString( Translation.tr("Leave the Game", source) ) );
        }

        source.sendMessage( ChatStyle.DARK_GREEN, "-----------------------------------------------------" );

    }

    @Command(aliases = {"create"}, usage = "", desc = "Create a new Game")
    @CommandPermissions("hcg.create")
    public void create(CommandContext args, CommandSource source) throws CommandException {

        Player player = plugin.getEngine().getPlayer( source.getName(), true );

        if ( player == null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You must be a Player!", source)) );
            return;
        }

        GameDifficulty difficutly = null;

        if ( args.getString(0).equalsIgnoreCase("easy") ) {
            difficutly = GameDifficulty.EASY;
        } else if ( args.getString(0).equalsIgnoreCase("normal") ) {
            difficutly = GameDifficulty.NORMAL;
        } else if ( args.getString(0).equalsIgnoreCase("hard") ) {
            difficutly = GameDifficulty.HARD;
        } else if ( args.getString(0).equalsIgnoreCase("hardcore") ) {
            difficutly = GameDifficulty.HARDCORE;
        } else {
            difficutly = GameDifficulty.NORMAL;
        }

        GameSize size = null;

        if ( args.getString(1).equalsIgnoreCase("tiny") ) {
            size = GameSize.TINY;
        } else if ( args.getString(1).equalsIgnoreCase("small") ) {
            size = GameSize.SMALL;
        } else if ( args.getString(1).equalsIgnoreCase("medium") ) {
            size = GameSize.MEDIUM;
        } else if ( args.getString(1).equalsIgnoreCase("big") ) {
            size = GameSize.BIG;
        } else {
            size = GameSize.MEDIUM;
        }

        Game game = new Game( player, difficutly, size );
        gameManager.addGame(game);

        source.sendMessage( ChatArguments.fromFormatString( Translation.tr("The Game is createt other Players can now Join the Game!", source) ) );

    }

    @Command(aliases = {"list"}, usage = "", desc = "")
    @CommandPermissions("hcg.list")
    public void list(CommandContext args, CommandSource source) throws CommandException {

        Player player = plugin.getEngine().getPlayer( source.getName(), true );

        if ( player == null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You must be a Player!", source)) );
            return;
        }

        source.sendMessage( ChatStyle.DARK_GREEN, "-----------------------------------------------------" );
        source.sendMessage( ChatStyle.DARK_GREEN, "List all Games" );
        source.sendMessage( ChatStyle.DARK_GREEN, "-----------------------------------------------------" );

        for ( Game game : gameManager.getGames() ) {

            source.sendMessage( ChatArguments.fromFormatString( Translation.tr("Owner : %0 Status : %1", source, game.getOwner().getName(), game.getStatus() ) ) );
            source.sendMessage( ChatArguments.fromFormatString( Translation.tr("Settings : Difficulty %0 Size %1", source, game.getDifficulty(), game.getSize() ) ) );

            String players = "";

            for ( Player player2 : game.getPlayers() ) {

                players += player2.getName() + " ";

            }

            source.sendMessage( ChatArguments.fromFormatString( Translation.tr("Players : %0", source, players ) ) );

            source.sendMessage( ChatStyle.DARK_GREEN, "-----------------------------------------------------" );
        }

    }

    @Command(aliases = {"join"}, usage = "", desc = "")
    @CommandPermissions("hcg.join")
    public void join(CommandContext args, CommandSource source) throws CommandException {

        Player player = plugin.getEngine().getPlayer( source.getName(), true );

        if ( player == null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You must be a Player!", source)) );
            return;
        }

        if ( gameManager.getGameByPlayer( player ) != null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You are allready in a Game!", source)) );
            return;
        }

        Player player2 = plugin.getEngine().getPlayer( args.getString(0), true );

        if (  player2 == null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("The Player was not found!", source)) );
            return;
        }

        Game game = gameManager.getGameByPlayer( player2 );

        if ( game == null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("The Player is not in a Game!!", source)) );
            return;
        }

        // TODO message
        //Translation.broadcast("{{GOLD}}%0 has joined the Game", game.getPlayers(), player.getName() );
        game.addPlayer( player );

        source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You joined the Game!", source)) );
    }

    @Command(aliases = {"leave"}, usage = "", desc = "")
    @CommandPermissions("hcg.leave")
    public void leave(CommandContext args, CommandSource source) throws CommandException {

        Player player = plugin.getEngine().getPlayer( source.getName(), true );

        if ( player == null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You must be a Player!", source)) );
            return;
        }

        Game game = gameManager.getGameByPlayer( player );

        if ( game == null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You are not in a Game!", source)) );
            return;
        }

        game.removePlayer(player);

        source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You leave the Game!", source)) );
    }

    @Command(aliases = {"start"}, usage = "", desc = "")
    @CommandPermissions("hcg.start")
    public void start(CommandContext args, CommandSource source) throws CommandException {

        Player player = plugin.getEngine().getPlayer( source.getName(), true );

        if ( player == null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You must be a Player!", source)) );
            return;
        }

        Game game = gameManager.getGameByPlayer( player );

        if ( game == null ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You are not in a Game!", source)) );
            return;
        }

        if ( game.getPlayers().size() == 1 ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("You are alone in the Game!", source)) );
            return;
        }

        if ( game.getStatus().equals("running") ) {
            source.sendMessage( ChatArguments.fromFormatString(Translation.tr("The Game is allready running!", source)) );
            return;
        }

        game.startGame();
    }

    @Command(aliases = {"test"}, usage = "", desc = "")
    @CommandPermissions("hcg.test")
    public void test(CommandContext args, CommandSource source) throws CommandException {

        Player player = plugin.getEngine().getPlayer( source.getName(), true );

        Game game = new Game(player, GameDifficulty.EASY, GameSize.TINY);
        game.startGame();


    }

    @Command(aliases = {"world"}, usage = "", desc = "")
    @CommandPermissions("hcg.world")
    public void world(CommandContext args, CommandSource source) throws CommandException {

        Player player = plugin.getEngine().getPlayer( source.getName(), true );

        player.sendMessage( "You are in World : " + player.getWorld().getName() );
        player.sendMessage( "Chunk : X " + player.getChunk().getX() + " Y " + player.getChunk().getY() + " Z " + player.getChunk().getZ() );

    }

}
