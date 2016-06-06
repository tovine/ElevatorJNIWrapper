#include <assert.h>
#include "no_ntnu_stud_torbjovn_comedielevator_NativeInterface.h"
#include "elev.h"

/*
 * Utility function to convert from int to elev_button_type_t, as I had to do it more than once
 */
elev_button_type_t getButtonType(int button) {
    elev_button_type_t buttonType;
    switch (button) {
        case BUTTON_CALL_UP:
            buttonType = BUTTON_CALL_UP;
            break;
        case BUTTON_CALL_DOWN:
            buttonType = BUTTON_CALL_DOWN;
            break;
        case BUTTON_COMMAND:
            buttonType = BUTTON_COMMAND;
            break;
        default:
            assert(0); // Invalid type, crash and burn
    }
    return buttonType;
}

/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_init
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1init
  (JNIEnv * env, jobject obj, jboolean simulated) {
    if (simulated) elev_init(ET_Simulation);
    else elev_init(ET_Comedi);
  }

/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_set_motor_direction
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1set_1motor_1direction
  (JNIEnv * env, jobject obj, jint dirn) {
    elev_motor_direction_t direction = DIRN_STOP;
    // Looks a bit redundant, but it's the least ugly way I could find to convert an int to an enum var
    switch(dirn) {
        case DIRN_DOWN:
            direction = DIRN_DOWN;
            break;
        case DIRN_UP:
            direction = DIRN_UP;
            break;
    }
    elev_set_motor_direction(direction);
  }
/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_set_button_lamp
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1set_1button_1lamp
  (JNIEnv * env, jobject obj, jint button, jint floor, jint value) {
    elev_set_button_lamp(getButtonType(button), floor, value);
  }

/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_set_floor_indicator
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1set_1floor_1indicator
  (JNIEnv * env, jobject obj, jint floor) {
    elev_set_floor_indicator(floor);
  }

/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_set_door_open_lamp
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1set_1door_1open_1lamp
  (JNIEnv * env, jobject obj, jint value) {
    elev_set_door_open_lamp(value);
  }

/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_set_stop_lamp
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1set_1stop_1lamp
  (JNIEnv * env, jobject obj, jint value) {
    elev_set_stop_lamp(value);
  }

/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_get_button_signal
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1get_1button_1signal
  (JNIEnv * env, jobject obj, jint button, jint floor) {
    return elev_get_button_signal(getButtonType(button), floor);
  }

/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_get_floor_sensor_signal
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1get_1floor_1sensor_1signal
  (JNIEnv * env, jobject obj) {
    return elev_get_floor_sensor_signal();
  }

/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_get_stop_signal
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1get_1stop_1signal
  (JNIEnv * env, jobject obj) {
    return elev_get_stop_signal();
  }

/*
 * Class:     no_ntnu_stud_torbjovn_comedielevator_NativeInterface
 * Method:    elev_get_obstruction_signal
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_no_ntnu_stud_torbjovn_comedielevator_NativeInterface_elev_1get_1obstruction_1signal
  (JNIEnv * env, jobject obj) {
    return elev_get_obstruction_signal();
  }