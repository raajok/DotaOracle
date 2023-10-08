package com.raajok;

import com.raajok.commands.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages setting up and executing all the slash commands.
 */
public class CommandManager extends ListenerAdapter {

    private List<Command> commandList = new ArrayList<>();

    public void addCommand(Command command) {
        this.commandList.add(command);
    }

    /**
     * Sets up all the commands added to CommandManager on bot startup
     * @param event
     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (Command command : this.commandList) {
                guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
            }
        }
    }

    /**
     * Executes the correct command from the Command list when a slash command is used
     * @param event
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (Command command : this.commandList) {
            if (event.getName().equals(command.getName())) {
                command.execute(event);
            }
        }
    }
}
