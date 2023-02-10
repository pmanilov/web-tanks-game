package com.manilov.gdx.game.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.manilov.gdx.game.Starter;
import com.manilov.gdx.game.client.ws.WsEvent;

public class MessageProcessor {
    private final Starter starter;

    public MessageProcessor(Starter starter) {
        this.starter = starter;
    }

    public void processEvent(WsEvent event) {
        String data = event.getData();
        if (data != null) {
            JSONValue parsed = JSONParser.parseStrict(data);
            JSONArray array = parsed.isArray();
            JSONObject object = parsed.isObject();
            if(array != null) {
                processArray(array);
            } else if (object != null) {
                processObject(object);
            }
        }
    }
    private void processArray(JSONArray array) {
        for (int i = 0; i < array.size(); i++) {
            JSONValue jsonValue = array.get(i);
            JSONObject object = jsonValue.isObject();
            if (object != null) {
                processObject(object);
            }
        }
    }
    private void processObject(JSONObject object) {
        JSONValue type = object.get("class");
        if (type != null) {
            switch (type.isString().stringValue()) {
                case "sessionKey":
                    String meId = object.get("id").isString().stringValue();
                    starter.setMeId(meId);
                    break;
                    case "disconnect":
                        String idToDisconnect = object.get("id").isString().stringValue();
                        starter.disconnect(idToDisconnect);
                    break;
                    case "tank":
                        float x = ((float) object.get("x").isNumber().doubleValue());
                        float y = ((float) object.get("y").isNumber().doubleValue());
                        float angle = ((float) object.get("angle").isNumber().doubleValue());
                        float angleBody = ((float) object.get("angleBody").isNumber().doubleValue());
                        String id = object.get("id").isString().stringValue();
                        boolean firePressed = object.get("firePressed").isBoolean().booleanValue();
                        starter.updateTank(id, x, y, angle, angleBody, firePressed);
                        break;
                /*case "particle":
                    float posX = ((float) object.get("x").isNumber().doubleValue());
                    float posY = ((float) object.get("y").isNumber().doubleValue());
                    float Angle = ((float) object.get("angle").isNumber().doubleValue());
                    String owner = object.get("owner").isString().stringValue();
                    starter.updateParticles(owner, posX, posY, Angle);
                    break;*/
                default:
                    throw new RuntimeException("Unknown message type " + type);
            }
        }
    }


}
