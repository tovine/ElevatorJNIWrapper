package no.ntnu.stud.torbjovn.comedielevator;

import cz.adamh.utils.NativeUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Native (C) wrapper class for the elevator driver provided in the course TTK4145 at NTNU.
 * Home: https://github.com/tovine/ElevatorJNIWrapper
 *
 * Written by Torbjørn Viem Ness, June 2016.
 */
public class NativeInterface {
	// "Enums"
	public static final int
			// Motor direction
			DIRN_DOWN = -1,
			DIRN_STOP = 0,
			DIRN_UP = 1,
			// Button/light type
			BUTTON_CALL_UP = 0,
			BUTTON_CALL_DOWN = 1,
			BUTTON_COMMAND = 2;

	/*
	 * Note: enums were my first solution (and is better, as it ensures only valid values), but it
	 * requires one extra C header file per type. I'm just leaving them here for reference.
	 */
//	public enum MotorDirection {
//		DIRN_DOWN (-1),
//		DIRN_STOP (0),
//		DIRN_UP (1);
//		final int val;
//		MotorDirection(int value) { this.val = value; }
//	}
//
//	public enum ButtonType {
//		BUTTON_CALL_UP (0),
//		BUTTON_CALL_DOWN (1),
//		BUTTON_COMMAND (2);
//		final int val;
//		ButtonType(int value) { this.val = value; }
//	}
//
//	public enum ElevatorType {
//		ET_COMEDI,
//		ET_SIMULATION
//	}

	static {
		try {
			NativeUtils.loadLibraryFromJar("/libelevator.so");
		} catch (IOException e) {
			if (FileNotFoundException.class.isInstance(e))
				System.out.println("ERROR: unable to read library file: " + e.getMessage());
			else
				e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * This constructor attempts to initialize the hardware (or the simulator) - if it fails, the C
	 * code will raise SIGABRT and kill the application (therefore it may be a good idea to do this
	 * before initializing anything else that needs to be explicitly closed or deallocated).
	 * @param simulated -- Whether or not to use the simulator or the actual hardware
	 */
	public NativeInterface(boolean simulated) {
		elev_init(simulated);
		System.out.println("Elevator initialization done");
	}

	public static void main(String[] args) {
		boolean simulated = false;
		for (String arg : args) {
			if (arg.matches("^\\-{1,2}sim(ulated|ulator|)$")) {
				simulated = true;
				System.out.println("Starting in simulator mode as requested...");
			}
		}
		NativeInterface nativeInterface = new NativeInterface(simulated);
	}

	/*
	 * The functions below that are commented out represent the variants that take enums as parameter
	 * instead of just primitive types.
	 * I decided not to use them as it would imply one extra C file (and headers) per enumerated type,
	 * however they are included just for reference in case you want to try it.
	 *
	 * In order to use them:
	 *   - uncomment them (and comment out the currently used ones)
	 *   - navigate to 'src/main' and run 'javah -stubs no.ntnu.stud.torbjovn.comedielevator.NativeInterface'
	 *     - Move the resulting header file to 'driver/' (replacing the existing one)
	 *   - update the implementation in 'driver/JNIFunctions.c' to match the new header file.
	 */
	public native void elev_init(boolean simulated);
//	public native void elev_init(ElevatorType e);

	public native void elev_set_motor_direction(int dirn);
//	public native void elev_set_motor_direction(MotorDirection dirn);
	public native void elev_set_button_lamp(int button, int floor, int value);
//	public native void elev_set_button_lamp(ButtonType button, int floor, int value);
	public native void elev_set_floor_indicator(int floor);
	public native void elev_set_door_open_lamp(int value);
	public native void elev_set_stop_lamp(int value);

	public native int elev_get_button_signal(int button, int floor);
//	public native int elev_get_button_signal(ButtonType button, int floor);
	public native int elev_get_floor_sensor_signal();
	public native int elev_get_stop_signal();
	public native int elev_get_obstruction_signal();

}
