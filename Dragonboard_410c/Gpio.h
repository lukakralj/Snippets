/**
 * Gpio class helps managing the GPIO pins on the Dragonboard 410c.
 * This will only work for the board that runs Debian. Must be ran as
 * sudo user.
 * 
 * @author Luka Kralj
 * @version 1.0
 */

#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
#include <cstring>
#include <cstdio>
#include <memory>
#include <stdexcept>
#include <string>
#include <array>

#define HIGH "1"
#define LOW "0"
#define OUT "out"
#define IN "in"

#define PATH_EXPORT "/sys/class/gpio/export"
#define PATH_UNEXPORT "/sys/class/gpio/unexport"

using namespace std;

string exec(const string);
int convertPhysicalPin(int);

class Gpio {

	private:
		string pinNo;
		string dir;

		string getDirPath();
		string getValuePath();
		void exportPin();

	public:
		Gpio(int, string);
		string getDirection();
		int readValue();
		void setHigh();
		void setLow();
		void unexportPin();
};

struct InvalidDirectionException : public exception {
   const char* what () const throw () {
      return "Unknown direction set to the pin.";
   }
};

struct InvalidPhysicalPinException : public exception {
   const char* what () const throw () {
      return "Invalid pin number - could not be exported.";
   }
};

struct InvalidOperationException : public exception {
   const char* what () const throw () {
      return "Invalid operation for this pin type.";
   }
};

/**
 * Initialise pin by exporting it and setting its direction.
 * In practice, pins won't change their direction often, hence changing
 * the direction can be done by unexporting the current pin and then re-initialise 
 * it again with a different direction.
 * 
 * @param physicalPin Physical number of the pin as shown on the board schematic; 
 * 						must be between 24 and 34 inclusive.
 * @param direction "out" for output pin, "in" for input pin.
 */
Gpio::Gpio(int physicalPin, string direction) {
	pinNo = to_string(convertPhysicalPin(physicalPin));
	if (direction != OUT && direction != IN) {
		throw InvalidDirectionException();
	}
	dir = direction;
	
	exportPin();

	// set direction
	string cmd = "echo " + dir + " > " + getDirPath();
	exec(cmd);
}

/**
 * Get direction of the pin.
 * 
 * @return "in" if this is input pin, "out" if this is output pin
 */
string Gpio::getDirection() {
	string cmd = "cat " + getDirPath();
	return exec(cmd);
}

/**
 * Return the current value at the pin.
 * 
 * @return "1" if the voltage at the pin is currently high, "0" if the voltage is low.
 */
int Gpio::readValue() {
	string cmd = "cat " + getValuePath();
	return (exec(cmd) == "1" ? 1 : 0); 
}

/**
 * Set high voltage on the pin (turn it "on").
 * 
 * @throws InvalidOperationException if this is an input pin.
 */
void Gpio::setHigh() {
	if (dir == IN) {
		throw InvalidOperationException();
	}
	string cmd = "echo 1 > " + getValuePath();
	exec(cmd); 
}

/**
 * Set low voltage on the pin (turn it "off").
 * 
 * @throws InvalidOperationException if this is an input pin.
 */
void Gpio::setLow() {
	if (dir == IN) {
		throw InvalidOperationException();
	}
	string cmd = "echo 0 > " + getValuePath();
	exec(cmd); 
}

/**
 * @return Absolute path for "direction" file of the pin.
 */
string Gpio::getDirPath() {
	return "/sys/class/gpio/gpio" + pinNo + "/direction";
}

/**
 * @return Absolute path for "value" file of the pin.
 */
string Gpio::getValuePath() {
	return "/sys/class/gpio/gpio" + pinNo + "/value";
}

/**
 * Exports the pin. Pin is ready to use after exporting.
 */
void Gpio::exportPin() {
	string cmd = "echo " + pinNo + " > /sys/class/gpio/export";
	exec(cmd);
}

/**
 * Unexports pin. Pin cannot be used after unexporting. Must
 * be called at the end of the program to clean up.
 */
void Gpio::unexportPin() {
	string cmd = "echo " + pinNo + " > /sys/class/gpio/unexport";
	exec(cmd);
}

/**
 * Converts physical pin number into a coresponding
 * system pin number.
 * 
 * @return Pin number that should be used in all terminal commands.
 */
int convertPhysicalPin(int pin) {
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
        default: throw InvalidPhysicalPinException();
    }
}

/**
 * Execute the command and return its stdout - or if there was
 * an error, return empty string. In case of an error the whole output
 * is written on the screen.
 * If the final character is a line break \n it will be removed.
 * 
 * @return Output of the command, or empty string if there was an error.
 */
string exec(string cmd) {
	cmd += " 2>&1";

	array<char, 128> buffer;
	string result;

	FILE* pipe = popen(cmd.c_str(), "r");
	if (!pipe) {
		throw runtime_error("popen() failed!");
	}
	while (fgets(buffer.data(), buffer.size(), pipe) != NULL) {
		result += buffer.data();
	}

	int exitCode = pclose(pipe);

	if (exitCode != 0) {
		// An error occurred.
		cout << "============" << endl;
		cout << "Command: '" << cmd << "' failed with exit code: " << exitCode << endl;
		cout << "Combined command output is:" << endl;
		cout << result << endl;
		cout << "============" << endl;
		result = "";
	}
	else {
		// Remove final line break \n if there is any.
		if (result.at(result.size() - 1) == '\n') {
			result = result.substr(0, result.size() - 1);
		}
	}

	return result;
}