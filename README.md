# RuneBot: A RuneScape-emulating Discord bot
<p><img src="https://i.imgur.com/OMgVAM6.jpg" width="200" height="200" /></img></p>

## What is it?
RuneBot is a Discord bot that aims to be an emulation of RuneScape, much like the concept behind [Pokébot](https://github.com/Wonder-Toast/Pokebot).

### [Click here to add the bot to your Discord!](https://tinyurl.com/AddRuneBot)

## Setup
A config.yaml file must be made in the source root. The format for the YAML file is as follows:
```
messageCooldown: SECONDS
jdbc:
  url: "jdbc:postgresql://ADDRESS:PORT(typically 5432)/DB_NAME"
  driver: "org.postgresql.Driver"
  username: "DB_USERNAME"
  password: "DB_PASSWORD"
discord:
  token: "TOKEN AS PER DISCORDAPP.COM"
```
You are also required to have a PostgreSQL database with a `players` table. The structure for the `players` table is as follows:
```
CREATE TABLE players (
	discord_id text NULL,
	total_level int4 NULL,
	total_exp int4 NULL,
	levels int4[] NULL,
	experiences int4[] NULL,
	item_ids int4[] NULL,
	item_counts int4[] NULL
)
```
Along with a PostgreSQL database, Kotlin must be installed and configured on your computer. After all of that, use the following command to run the bot (if you choose to use command line/terminal):
```
mvn exec:java -Dexec.mainClass="com.gmail.arhamjsiddiqui.runebot.RuneBot"
```

## Features
- Creation of a character, made by using any command. Your character is linked to your discord account.
- A highscore system (including the ability to toggle from guild-view to global-view)!
```
r.rank [Optional: "global"] [Optional: SKILL] [Optional: @USER] - gets the rank of a player in that certain skill or total level.
r.highscore | r.ranks [Optional: "global"] [Optional: SKILL] - gets the top 10 players for a certain skill or total level.
```
- An items system, including a command to check items you currently own (and the ability to check other players' too!).
```
r.items [Optional: @USER]
```
- Training of skills, with acquiring items that you have the requirements for while doing so.
```
r.train SKILL
```
The formula for EXP is the following:
```
A random number from 
((15 * SKILL_LEVEL) 
to 
((a 1% possibility of (15 * SKILL_LEVEL) to the power of 1.5) 
    OR ((15 * SKILL_LEVEL) to the power of 1.2))).
```
This includes the ability to view those skills too (with the perk of it being a cool generated image!) (and the added benefit of checking other players' too!):
```
r.skills [Optional: @USER]
```
![Sample of command generating image](https://i.imgur.com/uYlYykK.png)
- A help command to list all the commands available.
```
r.help
```
