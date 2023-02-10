package com.manilov.gdx.game.emitter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.manilov.gdx.game.Tank;

public class Particle implements Pool.Poolable{
    private String owner;
    private float size = 16;
    private float speed = 50;
    private float distance2;
   /* private float x;
    private float y;*/
    private float angle;
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

    /*public float getX() {
        return x;
    }*/

    /*public void setX(float x) {
        this.x = x;
    }*/

   /* public float getY() {
        return y;
    }*/

   /* public void setY(float y) {
        this.y = y;
    }*/

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isTank(ObjectMap<String, Tank> tanks){
        for (ObjectMap.Entry<String, Tank> tankEntry : tanks) {
            Tank tank = tankEntry.value;
            if (position.x >= tank.getPosition().x &&
                    position.x <= tank.getPosition().x + tank.getSize() &&
                    position.y >= tank.getPosition().y &&
                    position.y <= tank.getPosition().y + tank.getSize() &&
                !tank.getId().equals(owner)) {
                return true;
            }
        }
        return false;
    }
}
