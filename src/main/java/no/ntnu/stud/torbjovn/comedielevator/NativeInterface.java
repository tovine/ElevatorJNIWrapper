package no.ntnu.stud.torbjovn.comedielevator;

import cz.adamh.utils.NativeUtils;

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
			System.out.println("ERROR: unable to read library file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public NativeInterface() {
		if (!elev_init(false)) {
			System.out.println("Failed to initialize elevator hardware! Trying simulator...");
			if (!elev_init(true))
				throw new RuntimeException("Failed to initialize elevator driver, both hardware and simulator unavailable!");
			System.out.println("Simulator initialization done");
		} else
			System.out.println("HW initialization done");
	}

	public static void main(String[] args) {
		NativeInterface nativeInterface = new NativeInterface();
	}

	public native boolean elev_init(boolean simulated);
//	public native boolean elev_init(ElevatorType e);

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
