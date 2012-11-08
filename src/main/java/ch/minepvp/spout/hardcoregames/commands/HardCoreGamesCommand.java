package ch.minepvp.spout.hardcoregames.commands;

import ch.minepvp.spout.hardcoregames.HardCoreGames;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.NestedCommand;
import org.spout.api.exception.CommandException;

public class HardCoreGamesCommand {

    private final HardCoreGames plugin;

    public HardCoreGamesCommand( HardCoreGames instance) {
        plugin = instance;
    }

    @Command(aliases = {"hardcoregames", "hcg", "game"}, usage = "", desc = "HardCoreGames Commands", min = 1, max = 1)
    @NestedCommand(HardCoreGamesCommands.class) // Subcommands
    public void hardcoregames(CommandContext args, CommandSource source) throws CommandException {

    }

}
