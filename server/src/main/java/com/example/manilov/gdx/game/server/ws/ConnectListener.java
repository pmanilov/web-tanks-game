package com.example.manilov.gdx.game.server.ws;

import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

public interface ConnectListener {
    void handle(StandardWebSocketSession session);
}
