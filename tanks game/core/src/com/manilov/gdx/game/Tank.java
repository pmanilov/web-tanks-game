package com.manilov.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.manilov.gdx.game.emitter.Emitter;

import java.util.ArrayList;

public class Tank {
    private String id = "";
    private final float barrelX = 28;
    private final float centralY = 8;
    private final float barrelY = 30;
    private final float height = 58;
    private final float weight = 24;
    private final float halfweight = weight / 2;
    private static final float size = 80;
    private final Vector2 position = new Vector2();
    private final Vector2 angle = new Vector2(1, 1);
    private final Vector2 origin = new Vector2();
    private final Vector2 angleBody = new Vector2(1, 1);
    private final Texture body;
    private final Texture barrel;
    private final TextureRegion barrelRegion;
    private final TextureRegion bodyRegion;
    public Emitter emitter = new Emitter();
    private boolean firePressed = false;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isFirePressed() {
        return firePressed;
    }

    public void setFirePressed(boolean firePressed) {
        this.firePressed = firePressed;
    }

    public Tank(float x, float y) {
        this(x, y, "Blue");
    }

    public Tank(float x, float y, String color) {
        body = new Texture("tank"+color+".png");
        barrel = new Texture("barrel"+color+".png");
        barrelRegion = new TextureRegion(barrel);
        bodyRegion = new TextureRegion(body);
        position.set(x, y);
        origin.set(position).add(barrelX + halfweight, barrelY + centralY);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getAngle() {
        return angle;
    }

    public void render(Batch batch) {
        batch.draw(bodyRegion,
                position.x,
                position.y,
                barrelX + halfweight,
                barrelY + centralY,
                size,
                size,
                1,
                1,
                angleBody.angleDeg()
                );
        batch.draw(barrelRegion,
                   position.x + barrelX,
                   position.y + barrelY,
                    halfweight,
                   centralY,
                    weight,
                    height,
                  1,
                  1,
                   angle.angleDeg()
        );
    }

    public void dispose () {
        body.dispose();
        barrel.dispose();
    }

    public void moveTo(Vector2 direction,ArrayList<Obstacle> obstacles) {
        position.add(direction);
        if(Tank.isObs(position, obstacles) || Tank.notOnScreen(position)) {
            position.sub(direction);
        }
        origin.set(position).add(barrelX + halfweight, barrelY + centralY);
    }

    public void rotateTo(Vector2 mousePos) {
        angle.set(mousePos).sub(origin);
    }

    public void rotateBodyTo(Vector2 direction) {
        if(direction.x != 0 || direction.y != 0)
            angleBody.set(direction);
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
                position.x > Gdx.graphics.getWidth() - size ||
                position.y > Gdx.graphics.getHeight() - size)
            return true;
        return false;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void moveTo(float x, float y) {
        position.set(x, y);
        origin.set(position).add(barrelX + halfweight, barrelY + centralY);
    }

    public void rotateBodyTo(float angleBody) {
        this.angleBody.setAngleDeg(angleBody);
    }

    public void rotateTo(float angle) {
        this.angle.setAngleDeg(angle);
    }

    public float getSize(){
        return size;
    }

}
