package com.eve.eng1.util;

public class ExitValidation {
    private static boolean backpack_value = false;
    private static boolean door_validation = false;

    public static boolean getBackpack_value() {
        return backpack_value;
    }

    public static boolean getDoor_validation() {
        return door_validation;
    }

    public static void setBackpack_value(boolean backpack_value) {
        ExitValidation.backpack_value = backpack_value;
    }

    public static void setDoor_validation(boolean door_validation) {
        ExitValidation.door_validation = door_validation;
    }
}
