/**
 * This module allows other modules to register what happens when a certain command
 * is entered into the STDIN of this program. When a valid command is entered, all
 * actions associated with it are executed.
 * 
 * N.B. If two modules use the same main command name, the action will NOT be
 * overridden. Both 'subscribed' actions will be executed (like in C# - function 
 * subscription).
 * 
 * @module cli
 * @author Luka Kralj
 * @version 1.0
 */

module.exports = {
    registerCommand
};

const readline = require('readline');

const commands = {};

registerCommand("help", printHelp);
registerCommand("exit", onExit);

const cli = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
  terminal: false
});

cli.setPrompt("stdin@example >> ");

// Print initial help
setTimeout(() => {
    console.log("====Hello!====");
    console.log("Communicate with the program via this CLI.")
    console.log("Type 'help' to see all valid commands.");
    console.log("==============");
    cli.prompt();
}, 3000); // Wait a bit, to allow other modules to start.

cli.on('line', async (line) => {
    processLine(line);
});

/**
 * Resolves command action.
 * 
 * @param {string} line Line read.
 */
async function processLine(line) {
    line = line.trim();
    if (line.length == 0) {
        cli.prompt();
        return;
    }
    
    // parse main command
    const all = line.split(" ");
    const main = all[0];
    const params = (all.length > 1) ? all.slice(1) : [];

    if (commands.hasOwnProperty(main)) {
        // valid command
        executeAll(commands[main], params, "exit" != main);
    }
    else {
        console.log("Invalid command. Type 'help' for available commands.");
        cli.prompt();
    }
}

async function executeAll(actions, params, prompt) {
    const total = actions.length;
    let executed = 0;
    for (const i in actions) {
        actions[i](params).then(() => {
            executed++;
        });
    }

    while (executed < total) {
        await sleep(2);
    }
    if (prompt) {
        cli.prompt();
    }
}

/**
 * Register a new command.
 * 
 * @param {string} command Main command without parameters.
 * @param {function} action Will receive a list of optional parameters.
 */
async function registerCommand(command, action) {
    if (commands.hasOwnProperty(command)) {
        commands[command].push(action);
    }
    else {
        commands[command] = [action];
    }
}

/**
 * Displays valid commands.
 */
async function printHelp() {
    console.log("Valid commands are:");
    console.log(Object.keys(commands));
}

async function onExit() {
    cli.close();
    console.log("CLI closed.")
}

/**
 * Await for this function to pause execution for a certain time.
 *
 * @param {number} ms Time in milliseconds
 * @returns {Promise}
 */
function sleep(ms) {
    return new Promise(resolve => {
        setTimeout(resolve, ms);
    });
}
