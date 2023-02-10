package com.example.manilov.gdx.game.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.example.manilov.gdx.game.server.actors.Emitter;
import com.example.manilov.gdx.game.server.actors.Obstacle;
import com.example.manilov.gdx.game.server.actors.Particle;
import com.example.manilov.gdx.game.server.actors.Tank;
import com.example.manilov.gdx.game.server.ws.WebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

@Component
public class GameLoop extends ApplicationAdapter {
    private static final float frameRate = 1 / 60f;
    private final WebSocketHandler webSocketHandler;
    private float lastRender = 0;
    private final Json json;
    private final ObjectMap<String, Tank> tanks = new ObjectMap<>();
    private final Array<Tank> stateToSend = new Array<>();
    //private final Array<Particle> stateToSendBullet = new Array<>();
    private final ForkJoinPool pool = ForkJoinPool.commonPool();
    private static final ArrayList<Obstacle> obstacles = Obstacle.getObstacles();
    private static float spawn = 1740;

    public GameLoop(WebSocketHandler webSocketHandler, Json json) {
        this.webSocketHandler = webSocketHandler;
        this.json = json;
    }

    @Override
    public void create() {
        webSocketHandler.setConnectListener(session -> {
            Tank tank = new Tank(spawn, 450, session.getId());
            spawn += 1640;
            if(spawn > 1920)
                spawn = 100;
            tanks.put(session.getId(), tank);
            try {
                session.getNativeSession().getBasicRemote().sendText(String.format("{\"class\":\"sessionKey\",\"id\":\"%s\"}", session.getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        webSocketHandler.setDisconnectListener(session -> {
            sendToEverybody(String.format("{\"class\":\"disconnect\",\"id\":\"%s\"}", session.getId()));
            tanks.remove(session.getId());
        });
        webSocketHandler.setMessageListener((session, message) -> {
            pool.execute(() -> {
                String type = message.get("type").asText();
                switch (type) {
                    case "state":
                        Tank tank = tanks.get(session.getId());
                        tank.setLeftPressed(message.get("leftPressed").asBoolean());
                        tank.setRightPressed(message.get("rightPressed").asBoolean());
                        tank.setUpPressed(message.get("upPressed").asBoolean());
                        tank.setDownPressed(message.get("downPressed").asBoolean());
                        tank.setFirePressed(message.get("firePressed").asBoolean());
                        tank.setAngle((float) message.get("angle").asDouble());
                        break;
                    default:
                        throw new RuntimeException("Unknown WS Object type " + type);
                }
            });
        });
    }

    @Override
    public void render(){
        lastRender += Gdx.graphics.getDeltaTime();
        if (lastRender >= frameRate) {
            stateToSend.clear();
            //stateToSendBullet.clear();
            for (ObjectMap.Entry<String, Tank> tankEntry : tanks) {
                Tank tank = tankEntry.value;
                if(tank != null)
                    tank.act(lastRender, obstacles);
                stateToSend.add(tank);
                Emitter emitter = tank.emitter;
                emitter.setAngle(tank.getAngle() + 90);
                emitter.getPosition().set(tank.getPosition().x + 40, tank.getPosition().y + 38);
                emitter.start(lastRender, tank.isFirePressed());
                emitter.act(lastRender, obstacles, new ObjectMap<>(tanks));
                /*for(Particle particle: emitter.getParticles()) {
                    stateToSendBullet.add(particle);*/
                //}
            }
            lastRender = 0;
            String stateJson = json.toJson(stateToSend);
           // String bulletJson = json.toJson(stateToSendBullet);
            sendToEverybody(stateJson);
           // sendToEverybody(bulletJson);
        }
    }

    private void sendToEverybody(String json) {
        pool.execute(() ->{
            for (StandardWebSocketSession session : webSocketHandler.getSessions()) {
                try {
                    if(session.isOpen()){
                        session.getNativeSession().getBasicRemote().sendText(json);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
