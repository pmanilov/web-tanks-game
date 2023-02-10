package com.manilov.gdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private final Vector2 position = new Vector2();
    private final Texture texture;
    private final float size = 110;

    public Obstacle(float x, float y) {
        texture = new Texture("kamen.png");
        position.set(x, y);
    }

    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose () {
       texture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getSize() {
        return size;
    }
}
