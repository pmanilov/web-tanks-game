package com.manilov.gdx.game.emitter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.manilov.gdx.game.Obstacle;
import com.manilov.gdx.game.Starter;
import com.manilov.gdx.game.Tank;

import java.util.ArrayList;

public class Emitter {
    private String owner;
    private final Vector2 position = new Vector2();
    private float speed = 500;
    private float angle = 0;
    private float rate = 1;
    private float distance = 500;
    private float size = 13;
    private  float lastPatricleEmit = 0;
    private final DelayedRemovalArray<Particle> particles = new DelayedRemovalArray<>();
    private final Pool<Particle> particlePool = new Pool<Particle>() {
        @Override
        protected Particle newObject() {
            return new Particle();
        }
    };


    public void start(float delta, boolean isFirePressed) {
        if(isFirePressed && lastPatricleEmit >= 1 / rate) {
            Particle particle = particlePool.obtain();
            particle.fill(owner, position, angle, size, speed, distance);
            particles.add(particle);
            lastPatricleEmit = 0;
        }
        lastPatricleEmit += delta;
    }

    public void act(float delta, ArrayList<Obstacle> obstacles, ObjectMap<String, Tank> tanks) {
        particles.begin();
        for(Particle particle : particles) {
            particle.act(delta);
            if (particle.isFinished() || Emitter.isObs(particle.getPosition(), obstacles) || particle.isTank(tanks)) {
                particles.removeValue(particle, true);
                particlePool.free(particle);
            }
        }
        particles.end();;
    }

    private static boolean isObs(Vector2 position, ArrayList<Obstacle> obstacles) {
        for(int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if (position.x >= obstacle.getPosition().x &&
                    position.x <= obstacle.getPosition().x + obstacle.getSize() &&
                    position.y >= obstacle.getPosition().y &&
                    position.y <= obstacle.getPosition().y + obstacle.getSize()) return true;
        }
            return false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public DelayedRemovalArray<Particle> getParticles() {
        return particles;
    }

    public float getAngle() {
        return angle;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
