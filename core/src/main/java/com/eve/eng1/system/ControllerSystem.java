package com.eve.eng1.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.eve.eng1.asset.SoundAsset;
import com.eve.eng1.audio.AudioService;
import com.eve.eng1.component.Controller;
import com.eve.eng1.component.Move;
import com.eve.eng1.input.Commands;

public class ControllerSystem extends IteratingSystem {

    private final AudioService audioService;

    public ControllerSystem(AudioService audioService) {
        super(Family.all(Controller.class).get());
        this.audioService = audioService;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Controller controller = Controller.MAPPER.get(entity);
        if(controller.getPressedCommands().isEmpty() && controller.getReleasedCommands().isEmpty()){
            return; //Nothing to process
        }

        for (Commands pressedCommand : controller.getPressedCommands()) {
          switch (pressedCommand) {
            case UP -> moveEntity(entity, 0f, 1f);
            case DOWN -> moveEntity(entity, 0f, -1f);
            case LEFT -> moveEntity(entity, -1f, 0f);
            case RIGHT -> moveEntity(entity, 1f, 0f);
          }
        }
        controller.getPressedCommands().clear();

        //Values are inverted to make them become zero when pressed and unpressed
        for (Commands releasedCommand : controller.getReleasedCommands()) {

          switch (releasedCommand) {
            case UP -> moveEntity(entity, 0f, -1f);
            case DOWN -> moveEntity(entity, 0f, 1f);
            case LEFT -> moveEntity(entity, 1f, 0f);
            case RIGHT -> moveEntity(entity, -1f, 0f);

          }
        }
        controller.getReleasedCommands().clear();


    }

    private void moveEntity(Entity entity, float dX, float dY) {
        audioService.playSound(SoundAsset.WALKING);
        Move move = Move.MAPPER.get(entity);
        if(move == null) return;

        move.getDirection().x += dX;
        move.getDirection().y += dY;
    }
}
