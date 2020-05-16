const DISCORD = require('discord.js'),
    FILE_SYSTEM = require('fs'),
    YAML = require('js-yaml');
const CLIENT = new DISCORD.Client;

CLIENT.on('ready', () => console.log(`Logged in as ${CLIENT.user.tag}!`));

let fileContents = FILE_SYSTEM.readFileSync('./config.yaml');
let config = YAML.safeLoadAll(fileContents)[0];

CLIENT.login(config.token);