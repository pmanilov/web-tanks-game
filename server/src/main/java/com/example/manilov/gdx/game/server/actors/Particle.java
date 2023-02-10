package com.example.manilov.gdx.game.server.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

public class Particle implements Pool.Poolable/*, Json.Serializable*/{
    private String owner;
    private float size = 16;
    private float speed = 50;
    private float distance2;
    private final Vector2 position = new Vector2();
    private final Vector2 startPoint = new Vector2();
    private final Vector2 nextStepPoint = new Vector2(1, 1);
    public void act(float delta) {
        float stepLength = speed * delta;
        nextStepPoint.setLength(stepLength);
        position.add(nextStepPoint);
    }

    public void fill(String owner, Vector2 position, float angle, float size, float speed, float distance) {
        this.owner = owner;
        this.position.set(position);
        this.startPoint.set(position);
        this.nextStepPoint.setAngleDeg(angle);
        this.size = size;
        this.speed = speed;
        this.distance2 = distance * distance;
    }

    @Override
    public void reset() {
        this.owner = "";
        this.position.set(0, 0);
        this.startPoint.set(0, 0);
        this.nextStepPoint.set(1, 1);
        this.size = 0;
        this.speed = 0;
        this.distance2 = 0;
    }

    public boolean isFinished() {
        return position.dst2(startPoint) >= distance2;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getNextStepPoint() {
        return nextStepPoint;
    }

   /* @Override
    public void write(Json json) {
        json.writeValue("x", position.x);
        json.writeValue("y", position.y);
        json.writeValue("angle", nextStepPoint.angleDeg());
        json.writeValue("owner", owner);
    }

    @Override
    public void read(Json json, JsonValue jsonValue) {

    }*/

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isObs(ArrayList<Obstacle> obstacles) {
        for(int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if (position.x >= obstacle.getPosition().x &&
                    position.x <= obstacle.getPosition().x + obstacle.getSize() &&
                    position.y >= obstacle.getPosition().y &&
                    position.y <= obstacle.getPosition().y + obstacle.getSize()) return true;
        }
        return false;
    }
    public boolean isTank(ObjectMap<String, Tank> tanks){
        for (ObjectMap.Entry<String, Tank> tankEntry : tanks) {
            Tank tank = tankEntry.value;
            if (position.x >= tank.getPosition().x &&
                    position.x <= tank.getPosition().x + tank.getSize() &&
                    position.y >= tank.getPosition().y &&
                    position.y <= tank.getPosition().y + tank.getSize() &&
                    !owner.equals(tank.getId())) {
                tank.shooted();
                return true;
            }
        }
        return false;
    }
}
