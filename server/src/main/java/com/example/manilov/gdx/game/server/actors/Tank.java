package com.example.manilov.gdx.game.server.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

public class Tank implements Json.Serializable {
    private String id;

    private float angle = 0;
    private int hp;
    private float angleBody = 0;
    private final Vector2 position = new Vector2(100, 450);
    private final Vector2 direction = new Vector2();

    private int speed = 300;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean firePressed = false;
    private float spawn;
    private static float size = 80;
    public Emitter emitter;

    public Tank(float x, float y, String id) {
        position.set(x, y);
        this.id = id;
        emitter = new Emitter(id);
        this.spawn = x;
        this.hp = 3;
    }

    public Tank() {
    }

    public void act(float delta, ArrayList<Obstacle> obstacles) {
        if(this.hp <= 0) {
            position.set(spawn, 450);
            this.hp = 3;
        }
        direction.set(0, 0);
        float stepLength = speed * delta;
        if (isLeftPressed()) direction.x -= stepLength;
        if (isRightPressed()) direction.x += stepLength;
        if (isUpPressed()) direction.y += stepLength;
        if (isDownPressed()) direction.y -= stepLength;
        position.add(direction);
        if (direction.x != 0 || direction.y != 0)
            angleBody = direction.angleDeg() - 90;
        if (Tank.isObs(position, obstacles) || Tank.notOnScreen(position)) {
            position.sub(direction);
        }
    }

    public float getSpawn() {
        return spawn;
    }

    public void shooted() {
        this.hp--;
    }

    public static boolean isObs(Vector2 position, ArrayList<Obstacle> obstacles) {
        for(int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if(position.x >= obstacle.getPosition().x &&
                    position.x <= obstacle.getPosition().x + obstacle.getSize() &&
                    position.y >= obstacle.getPosition().y &&
                    position.y <= obstacle.getPosition().y + obstacle.getSize()) return true;
            if(position.x + size >= obstacle.getPosition().x &&
                    position.x + size <= obstacle.getPosition().x + obstacle.getSize() &&
                    position.y + size >= obstacle.getPosition().y &&
                    position.y + size <= obstacle.getPosition().y + obstacle.getSize()) return true;
            if(position.x + size >= obstacle.getPosition().x &&
                    position.x + size <= obstacle.getPosition().x + obstacle.getSize() &&
                    position.y >= obstacle.getPosition().y &&
                    position.y <= obstacle.getPosition().y + obstacle.getSize()) return true;
            if(position.x >= obstacle.getPosition().x &&
                    position.x <= obstacle.getPosition().x + obstacle.getSize() &&
                    position.y + size >= obstacle.getPosition().y &&
                    position.y + size <= obstacle.getPosition().y + obstacle.getSize()) return true;
        }
        return false;
    }

    public static boolean notOnScreen(Vector2 position) {
        if(position.x < 0 || position.y < 70 ||
                position.x > 1920 - size ||
                position.y > 1010 - size - 70)
            return true;
        return false;
    }

    public float getAngleBody() {
        return angleBody;
    }

    public void setAngleBody(float angleBody) {
        this.angleBody = angleBody;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isFirePressed() {
        return firePressed;
    }

    public void setFirePressed(boolean firePressed) {
        this.firePressed = firePressed;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void write(Json json) {
        json.writeValue("x", position.x);
        json.writeValue("y", position.y);
        json.writeValue("angle", angle);
        json.writeValue("angleBody", angleBody);
        json.writeValue("firePressed", firePressed);
        json.writeValue("id", id);
    }

    @Override
    public void read(Json json, JsonValue jsonValue) {

    }

    public static float getSize() {
        return size;
    }
}
