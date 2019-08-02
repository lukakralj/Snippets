/**
 * Represents a GPIO object that can be used in other modules.
 * When using this module the application needs to be run as a
 * sudo user otherwise it will not work.
 * 
 * @module 
 * @author Luka Kralj
 * @version 1.0
 */

const exec = require('child_process').exec;

const DIR_OUT = "out";
const DIR_IN = "in";

const gpioPath = "/sys/class/gpio";
const exportPath = gpioPath + "/export";
const unexportPath = gpioPath + "/unexport";

module.exports = {
    DIR_OUT,
    DIR_IN
}

module.exports.Gpio = class Gpio {

    constructor(physicalPin, direction) {
        this.pinNo = convertPhysicalPin(physicalPin);

        // setup pin
        if (direction != DIR_IN && direction != DIR_OUT) {
            throw new Error("Invalid Gpio direction: " + direction + ".");
        }
        this.direction = direction;
    }

    async init() {
        // export pin
        let ok = await exportPin(this.pinNo);
        if (!ok) throw new Error("Pin " + this.pinNo + " could not be exported.");
        // set direction
        ok = await cmdOutput(`echo ${this.direction} > ${getPinDirectionPath(this.pinNo)}`);
        if (!ok) {
            await unexportPin(this.pinNo);
            throw new Error("Pin direction for pin " + this.pinNo + " could not be set.");
        }
    }

    async turnOn() {
        if (this.direction == DIR_IN) {
            throw new Error("Invalid operation for 'IN' pin.");
        }
        const ok = await cmdOutput(`echo 1 > ${await getPinValuePath(this.pinNo)}`);
        if (!ok) return false;
        return ok;
    }

    async turnOff() {
        if (this.direction == DIR_IN) {
            throw new Error("Invalid operation for 'IN' pin.");
        }
        const ok = await cmdOutput(`echo 0 > ${await getPinValuePath(this.pinNo)}`);
        if (!ok) return false;
        return ok;
    }

    async isOn() {
        if (this.direction == DIR_IN) {
            throw new Error("Invalid operation: calling isOn on an 'IN' pin. Use readValue instead.");
        }
        const val = await cmdOutput(`cat ${await getPinValuePath(this.pinNo)}`);
        return val == 1;
    }

    async readValue() {
        if (this.direction == DIR_OUT) {
            throw new Error("Invalid operation: calling readValue on an 'OUT' pin. Use isOn instead.");
        }
        return await cmdOutput(`cat ${await getPinValuePath(this.pinNo)}`);
    }

    async unexport() {
        return await unexportPin(this.pinNo);
    }
}

function convertPhysicalPin(pin) {
    switch (pin) {
        case 24: return 12; // B
        case 25: return 13; // C
        case 26: return 69; // D 
        case 27: return 115; // E
        case 28: return 4; // F
        case 29: return 24; // G 
        case 30: return 25; // H 
        case 31: return 35; // I
        case 32: return 34; // J
        case 33: return 28; // K
        case 34: return 33; // L
        default: throw new Error("Invalid physical pin number: " + pin);
    }
}

async function exportPin(pin) {
    return await cmdOutput(`echo ${pin} > ${exportPath}`);
}

async function unexportPin(pin) {
    return await cmdOutput(`echo ${pin} > ${unexportPath}`);
}

function getPinValuePath(pin) {
    return `${gpioPath}/gpio${pin}/value`;
}

function getPinDirectionPath(pin) {
    return `${gpioPath}/gpio${pin}/direction`;
}

/**
 * Executes the given command and returns the response of the command.
 * 
 * @param {string} cmd One of the constants above.
 * @param {number} timeout Number of milliseconds.
 * @returns {string} Command output or undefined if an error occurred.
 */
async function cmdOutput(cmd, timeout = 10000) {
    let output = undefined;
    let finished = false;
    const proc = exec(cmd, (err, stdout, stderr) => {
        if (err) {
            console.log(err)
            console.log(stderr);
        }
        else {
            if (stdout === undefined || stdout.trim().length == 0) {
                // some commands might have empty output
                stdout = true;
            }
            output = stdout;
        }
        finished = true;
    });

    let time = 0;
    while (!finished && time < timeout) {
        time++;
        await sleep(1);
    }
    return output;
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