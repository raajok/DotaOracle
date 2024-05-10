package com.raajok;

import com.raajok.audio.AudioBot;
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
        AudioBot audioBot = new AudioBot();
        addCommands(cmdManager);

        jda.addEventListener(cmdManager);
        jda.addEventListener(audioBot);
    }

    private static void addCommands(CommandManager cmdManager) {
        cmdManager.addCommand(new SearchCommand());
        cmdManager.addCommand(new WlCommand());
        cmdManager.addCommand(new HelpCommand());
        cmdManager.addCommand(new HerostatsCommand());
        cmdManager.addCommand(new LeipaCommand());
    }
}
