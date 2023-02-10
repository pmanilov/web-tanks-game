package com.manilov.gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.manilov.gdx.game.emitter.Emitter;
import com.manilov.gdx.game.emitter.Particle;

import java.util.ArrayList;
import java.util.List;

public class Starter extends ApplicationAdapter {
	SpriteBatch batch;
	private String meId;
	private ObjectMap<String, Tank> tanks = new ObjectMap<>();
	private Array<Particle> particles = new Array<>();

	private final KeyboardAdapter inputProcessor;
	private MessageSender messageSender;

	private ArrayList<Obstacle> obstacles = new ArrayList<>();

	private Texture bulletTexture;
	private TextureRegion bulletTextureRegion;
	private Texture background;
	public Starter(InputState inputState) {
		this.inputProcessor = new KeyboardAdapter(inputState);
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(inputProcessor);
		batch = new SpriteBatch();
		Tank me = new Tank(100, 450);
		me.emitter.setOwner(meId);
		tanks.put(meId, me);
		float initX = 350;
		float initY = 300;
		for(int i = 0; i < 5; i++) {
			obstacles.add(new Obstacle(initX, initY));
			initY += 80;
		}
		initX = Gdx.graphics.getWidth() - 430;
		initY = 300;
		for(int i = 0; i < 5; i++) {
			obstacles.add(new Obstacle(initX, initY));
			initY += 80;
		}
		bulletTexture = new Texture("bullet.png");
		bulletTextureRegion = new TextureRegion(bulletTexture);
		background = new Texture("dirt.png");
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	@Override
	public void render () {
		/*Tank me = tanks.get(meId);*/
		//me.moveTo(inputProcessor.getDirection(), obstacles);
		//me.rotateBodyTo(inputProcessor.getDirection());
		//me.rotateTo(inputProcessor.getMousePos());
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		batch.draw(background,0,0);
		obstacles.forEach(obstacle -> {
			obstacle.render(batch);
		});
		/*Emitter emitter = me.emitter;
		emitter.setAngle(me.getAngle().angleDeg() + 90);
		emitter.getPosition().set(me.getPosition().x + 40, me.getPosition().y + 38);
		emitter.start(Gdx.graphics.getDeltaTime(), inputProcessor.isFirePressed());
		emitter.act(Gdx.graphics.getDeltaTime(), obstacles);*/
		/*for (Particle particle : particles) {
			batch.draw(bulletTextureRegion,
					particle.getX() - 4,
					particle.getY() - 5,
							5,
					5,
					13,
					13,
					1,
					1,
					particle.getAngle() - 90);
			particles.removeValue(particle, true);
		}*/
		for (String key : tanks.keys()) {
			Emitter emitter = tanks.get(key).emitter;
			emitter.setAngle(tanks.get(key).getAngle().angleDeg() + 90);
			emitter.getPosition().set(tanks.get(key).getPosition().x + 40, tanks.get(key).getPosition().y + 38);
			emitter.start(Gdx.graphics.getDeltaTime(), tanks.get(key).isFirePressed());
			emitter.act(Gdx.graphics.getDeltaTime(), obstacles, tanks);
			for (Particle particle : emitter.getParticles()) {
				batch.draw(bulletTextureRegion,
						particle.getPosition().x - 4,
						particle.getPosition().y - 5,
						5,
						5,
						13,
						13,
						1,
						1,
						particle.getNextStepPoint().angleDeg() - 90);
			}
			tanks.get(key).render(batch);
		}
		batch.end();
		particles.clear();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		for (Tank value : tanks.values()) {
			value.dispose();;
		}
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public void handleTimer() {
		if (inputProcessor != null && !tanks.isEmpty()) {
			Tank me = tanks.get(meId);
			InputState playerState = inputProcessor.updateAndGetInputState(me.getOrigin());
			messageSender.sendMessage(playerState);
		}
	}

    public void setMeId(String meId) {
		this.meId = meId;
    }

	public void disconnect(String idToDisconnect) {
		tanks.remove(idToDisconnect);
	}

	public void updateTank(String id, float x, float y, float angle, float angleBody, boolean firePressed) {
		if (tanks.isEmpty())
			return;
		Tank tank = tanks.get(id);
		if(tank == null){
			tank = new Tank(x, y, "Red");
			tanks.put(id, tank);
		} else {
			tank.moveTo(x, y);
		}
		tank.rotateBodyTo(angleBody);
		tank.rotateTo(angle);
		tank.setId(id);
		tank.emitter.setOwner(id);
		tank.setFirePressed(firePressed);
	}

	/*public void updateParticles(String owner, float posX, float posY, float angle) {
		if (tanks.isEmpty())
			return;
		Particle particle = new Particle();
		particle.setAngle(angle);
		particle.setX(posX);
		particle.setY(posY);
		particles.add(particle);

	}*/
}
