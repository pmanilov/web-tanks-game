package com.example.manilov.gdx.game.server.config;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.example.manilov.gdx.game.server.GameLoop;
import com.example.manilov.gdx.game.server.actors.Particle;
import com.example.manilov.gdx.game.server.actors.Tank;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public HeadlessApplication getApplication(GameLoop gameLoop) {
        return new HeadlessApplication(gameLoop);
    }

    @Bean
    public Json getJson() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("tank", Tank.class);
        /*json.addClassTag("particle", Particle.class);*/
        return json;
    }
}
