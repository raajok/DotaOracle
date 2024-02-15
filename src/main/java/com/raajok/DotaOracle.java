package com.raajok;

import com.raajok.commands.*;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class DotaOracle {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        final String TOKEN = dotenv.get("DISCORD_TOKEN");

        JDA jda = JDABuilder.createDefault(TOKEN).build();

        CommandManager cmdManager = new CommandManager();
        addCommands(cmdManager);

        jda.addEventListener(cmdManager);
    }

    private static void addCommands(CommandManager cmdManager) {
        cmdManager.addCommand(new SearchCommand());
        cmdManager.addCommand(new WlCommand());
        cmdManager.addCommand(new HelpCommand());
        cmdManager.addCommand(new HerostatsCommand());
        cmdManager.addCommand(new LeipaCommand());
    }
}
