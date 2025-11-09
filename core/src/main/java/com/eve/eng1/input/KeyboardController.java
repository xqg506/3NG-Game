package com.eve.eng1.input;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class KeyboardController extends InputAdapter {
    private static final Map<Integer, Commands> KEY_MAPPING = Map.ofEntries(
        Map.entry (Input.Keys.W, Commands.UP),
        Map.entry (Input.Keys.A, Commands.LEFT),
        Map.entry (Input.Keys.S, Commands.DOWN),
        Map.entry (Input.Keys.D, Commands.RIGHT)
    );

    private final boolean[] commandState;
    private final Map<Class<? extends ControllerState>, ControllerState> stateCache;
    private ControllerState activeState;

    public KeyboardController(Class<? extends ControllerState> initialState, Engine engine) {
        this.stateCache = new HashMap<>();
        this.activeState = null;
        this.commandState = new boolean[Commands.values().length];

        this.stateCache.put(IdleControllerState.class, new IdleControllerState());
        this.stateCache.put(GameControllerState.class, new GameControllerState(engine));
        setActiveState(initialState);
    }

    //This can be used to change state depending on what you are doing. Cutscene? Reimplement the idle one unless no controllers. In game - WASD gamecontrollerstate and so on.
    public void setActiveState(Class<? extends ControllerState> stateClass) {
        ControllerState controllerState = stateCache.get(stateClass);
        if (controllerState == null) {
            throw new GdxRuntimeException("No state with" + stateClass + "class found in the state cache");
        }
        
        for (Commands command : Commands.values()) {
            if (this.activeState!=null && this.commandState[command.ordinal()]){
                this.activeState.keyUp(command);
            }
            this.commandState[command.ordinal()] = false;
        }
        
        this.activeState = controllerState;
    }

    @Override
    public boolean keyDown(int keycode) {
        Commands command = KEY_MAPPING.get(keycode);
        if (command == null) return false;

        this.commandState[command.ordinal()] = true;

        this.activeState.keyDown(command);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        Commands command = KEY_MAPPING.get(keycode);
        if (command == null) return false;
        
        if (!this.commandState[command.ordinal()]) return false;

        this.commandState[command.ordinal()] = false;
        
        this.activeState.keyUp(command);
        return true;

    }

}
