package com.eve.eng1.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.eve.eng1.component.Controller;

public class GameControllerState implements ControllerState {
        private final ImmutableArray<Entity> controllerEntities;

        public GameControllerState(Engine engine) {
            this.controllerEntities = engine.getEntitiesFor(Family.all(Controller.class).get());
        }

        @Override
        public void keyDown(Commands command) {
            for (Entity entity : controllerEntities) { 
                Controller.MAPPER.get(entity).getPressedCommands().add(command);
            }
        }

        @Override
        public void keyUp(Commands command) {
            for (Entity entity : controllerEntities) {
                Controller.MAPPER.get(entity).getReleasedCommands().add(command);
            }
        }
    }