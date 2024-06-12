package com.raajok.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AudioBot extends ListenerAdapter {

    private final AudioPlayerManager playerManager;
    private final AudioPlayer player;
    private AudioTrack track;
    private AudioManager audioManager;
    private final AudioSendHandler audioSendHandler;
    private final TrackScheduler trackScheduler;
    private boolean trackPlayed;
    private final String roleToListen;

    public AudioBot() {
        this.playerManager = new DefaultAudioPlayerManager();
        this.player = playerManager.createPlayer();
        this.audioSendHandler = new AudioPlayerSendHandler(this.player);
        this.trackScheduler = new TrackScheduler();
        this.trackPlayed = false;

        this.roleToListen = "Leipägang";
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        AudioSourceManagers.registerLocalSource(playerManager);
        // AudioSourceManagers.registerRemoteSources(playerManager);

        Dotenv dotenv = Dotenv.load();
        String AUDIOFILE = dotenv.get("LEIPAJOIN_AUDIOFILE");

        playerManager.loadItem(AUDIOFILE, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                loadTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        AudioChannelUnion joinedChannel = event.getChannelJoined();
        Member member = event.getMember();
        List<Role> roles = member.getRoles();
        if (joinedChannel != null && isLeipa(roles)) {
            // Only react when member joins the channel (guildVoiceUpdateEvent also listens to leaving the channel)
            if (player.getPlayingTrack() != null) {
                // Already playing a track, ignore
                return;
            }

            Guild guild = event.getGuild();
            AudioManager audioManager = getOrCreateAudioManager(guild);
            audioManager.setSendingHandler(audioSendHandler);
            audioManager.openAudioConnection(joinedChannel);

            if (trackPlayed) {
                // can't play the same track twice
                track = track.makeClone();
            } else {
                player.addListener(trackScheduler);
                trackScheduler.setAudioManager(audioManager);
            }

            player.playTrack(track);
            trackPlayed = true;
        }
    }

    private void loadTrack(AudioTrack track) {
        this.track = track;
    }

    private AudioManager getOrCreateAudioManager(Guild guild) {
        if (this.audioManager == null) {
            this.audioManager = guild.getAudioManager();
        }

        return this.audioManager;
    }

    private boolean isLeipa(List<Role> roles) {
        for (Role role: roles) {
            if (role.getName().equals(roleToListen)) {
                return true;
            }
        }
        return false;
    }
}
