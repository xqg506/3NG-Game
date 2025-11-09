package com.eve.eng1.component;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.eve.eng1.input.Commands;

public class Controller implements Component {
    public static final ComponentMapper<Controller> MAPPER = ComponentMapper.getFor(Controller.class);

    private final List<Commands> pressedCommands;
    private final List<Commands> releasedCommands;

    public Controller() {
        this.pressedCommands = new ArrayList<>();
        this.releasedCommands = new ArrayList<>();
    }

    public List<Commands> getPressedCommands() {
        return pressedCommands;
    }

    public List<Commands> getReleasedCommands() {
        return releasedCommands;
    }
}