package com.example.manilov.gdx.game.server.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Obstacle {
    private final Vector2 position = new Vector2();
    private final float size = 110;

    public Obstacle(float x, float y) {
        position.set(x, y);
    }

    public static ArrayList<Obstacle> getObstacles() {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        float initX = 350;
        float initY = 300;
        for(int i = 0; i < 5; i++) {
            obstacles.add(new Obstacle(initX, initY));
            initY += 80;
        }
        initX = 1920 - 430;
        initY = 300;
        for(int i = 0; i < 5; i++) {
            obstacles.add(new Obstacle(initX, initY));
            initY += 80;
        }
        return obstacles;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getSize() { return size; }

}
