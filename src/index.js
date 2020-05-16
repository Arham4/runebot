const DISCORD = require('discord.js'),
    FILE_SYSTEM = require('fs'),
    YAML = require('js-yaml');
const CLIENT = new DISCORD.Client;

CLIENT.on('ready', () => console.log(`Logged in as ${CLIENT.user.tag}!`));

const FILE_CONTENTS = FILE_SYSTEM.readFileSync('./config.yaml');
const CONFIG = YAML.safeLoadAll(FILE_CONTENTS)[0];

CLIENT.login(CONFIG.token);