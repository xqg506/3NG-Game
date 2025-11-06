package com.eve.eng1.input;

public interface ControllerState {
        void keyDown(Commands command);

        default void keyUp(Commands command) {

        }

    }