# Dota Oracle
Dota Oracle is my ongoing Discord bot project. For now, it provides Dota 2 specific statistics via slash commands and plays music in voice channels.
## Commands
Below is a list of all commands with descriptions. Required parameters are in square brackets [] and optional parameters are in curly brackets {}.
| Command | Description |
| ----------- | ----------- |
| github | Gives a link to the bot's GitHub repository. |
| help | Sends information about the commands. |
| herostats [hero] [id] | Shows hero-specific stats for the player owning the Steam ID given. |
| leipägang | Shows my friend group's win/loss statistics and brief statistics for each member individually. |
| records | Shows the records of my friend group. The records are taken from the Dotabuff's record page. |
| search [name] | Searches players by name and gives top 5 results and their Steam IDs. Mostly used for copy-pasting SteamIDs for other commands. |
| wl [id] {amount} | Shows wins, losses and win percent for the player owning the Steam ID given. Can be limited to {amount} of recent games. |
## Other features
Whenever anyone from my friend group joins a voice channel, Dota Oracle joins as well and plays the group's anthem.
## Acknowledgements
- [JDA](https://github.com/discord-jda/JDA) is used to work with Discord.
- [OpenDota](https://www.opendota.com/)'s API provides most of the Dota 2 statistics.
- [Lavaplayer](https://github.com/lavalink-devs/lavaplayer) is used to play music in voice channels.
- [jsoup](https://jsoup.org/) is used to scrape and parse HTML for the records command.
