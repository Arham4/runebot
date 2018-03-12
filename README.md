# RuneBot: A RuneScape-emulating Discord bot
<img src="https://i.imgur.com/OMgVAM6.jpg" width="200" height="200" /></img>

## What is it?
RuneBot is a Discord bot that aims to be an emulation of RuneScape, much like the concept behind [Pokébot](https://github.com/Wonder-Toast/Pokebot).

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
After that, simply run RuneBot.kt!

## Features
- Creation of a character, made by using any command. Your character is linked to your discord account.
- An items system, including a command to check items available (and the ability to check other players' too!).
```
r.items [Optional: @USER]
```
- Training of skills.
```
r.train SKILL
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
