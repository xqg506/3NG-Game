package com.eve.eng1.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.eve.eng1.component.Physic;
import com.eve.eng1.component.Player;
import com.eve.eng1.component.Transform;
import com.eve.eng1.component.Trigger;

public class PhysicSystem extends IteratingSystem implements EntityListener, ContactListener {
    private final World world;
    private final float interval;

    //Sum of delta times per frame
    private float accumulator;

    public PhysicSystem(World world, float interval) {
        super(Family.all(Physic.class, Transform.class).get());
        this.world = world;
        this.interval = interval;
        this.accumulator = 0f;
        world.setContactListener(this);

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(getFamily(), this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
    }

    @Override
    public void entityAdded(Entity entity) {

    }

    @Override
    public void entityRemoved(Entity entity) {
        Physic physic = Physic.MAPPER.get(entity);
        if (physic != null) {
            this.world.destroyBody(physic.getBody());
        }
    }

    @Override
    public void update(float deltaTime) {
        this.accumulator += deltaTime;

        while (this.accumulator >= this.interval) {
            this.accumulator -= this.interval;
            super.update(deltaTime);
            this.world.step(interval, 6, 2);
        }
        world.clearForces();

        // Interpolation - a value between 0 and (nearly) 1.
        float alpha = this.accumulator / this.interval;
        for (int i = 0; i < getEntities().size(); i++) {
            this.interpolateEntity(getEntities().get(i), alpha);
        }

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Physic physic = Physic.MAPPER.get(entity);
        physic.getPrevPosition().set(physic.getBody().getPosition());
    }

    private void interpolateEntity(Entity entity, float alpha) {
        Transform transform = Transform.MAPPER.get(entity);
        Physic physic = Physic.MAPPER.get(entity);

        transform.getPosition().set(
            MathUtils.lerp(physic.getPrevPosition().x, physic.getBody().getPosition().x, alpha),
            MathUtils.lerp(physic.getPrevPosition().y, physic.getBody().getPosition().y, alpha)
        );
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Body bodyA = fixtureA.getBody();
        Object userDataA = bodyA.getUserData();

        Fixture fixtureB = contact.getFixtureB();
        Body bodyB = fixtureB.getBody();
        Object userDataB = bodyB.getUserData();

        if(!(userDataA instanceof Entity entityA) || !(userDataB instanceof Entity entityB)){
            return;
        }

        playerTriggerContact(entityA, fixtureA, entityB, fixtureB);

    }

    private void playerTriggerContact(Entity entityA, Fixture fixtureA, Entity entityB, Fixture fixtureB) {
        Trigger trigger = Trigger.MAPPER.get(entityA);
        boolean isPlayer = Player.MAPPER.get(entityB) != null && !fixtureB.isSensor();
        if(trigger != null && isPlayer){
            trigger.setTriggeringEntity(entityB);
            return;
        }

        trigger = Trigger.MAPPER.get(entityB);
        isPlayer = Player.MAPPER.get(entityA) != null && !fixtureA.isSensor();
        if(trigger != null && isPlayer){
            trigger.setTriggeringEntity(entityA);
        }

    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
