package com.eve.eng1.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.eve.eng1.asset.SoundAsset;
import com.eve.eng1.audio.AudioService;
import com.eve.eng1.component.Tiled;
import com.eve.eng1.component.Trigger;

public class TriggerSystem extends IteratingSystem {
    private final AudioService audioService;

    public TriggerSystem(AudioService audioService) {
        super(Family.all(Trigger.class).get());
        this.audioService = audioService;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Trigger trigger = Trigger.MAPPER.get(entity);
        if (trigger.getTriggeringEntity() == null) return;
        fireTrigger(trigger.getName(), trigger.getTriggeringEntity());
        trigger.setTriggeringEntity(null);
    }

    private void fireTrigger(String triggerName, Entity triggeringEntity) {
        switch (triggerName) {
            case "backpack_trigger" -> executeBackpackScript(triggeringEntity);
            default -> throw new GdxRuntimeException("Unsupported trigger: " + triggerName);
        }
    }

    private void executeBackpackScript(Entity triggeringEntity) {
        System.out.println("executing backpack script");
        Entity backpackEntity = entityByTiledId(15);
        if (backpackEntity == null) return;

        audioService.playSound(SoundAsset.SWING);

        getEngine().removeEntity(backpackEntity);

    }

    private Entity entityByTiledId(int tiledID) {
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(Family.all(Tiled.class).get());
        for(Entity entity : entities) {
            if (Tiled.MAPPER.get(entity).getId() == tiledID) {
                return entity;
            }
        }
        return null;
    }
}
