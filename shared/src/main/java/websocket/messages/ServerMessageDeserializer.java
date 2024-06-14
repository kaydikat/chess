package websocket.messages;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {
  @Override
  public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    String messageTypeString = jsonObject.get("serverMessageType").getAsString();
    ServerMessage.ServerMessageType messageType = ServerMessage.ServerMessageType.valueOf(messageTypeString);

    return switch (messageType) {
      case NOTIFICATION -> context.deserialize(jsonElement, NotificationMessage.class);
      case ERROR -> context.deserialize(jsonElement, ErrorMessage.class);
      case LOAD_GAME -> context.deserialize(jsonElement, LoadGameMessage.class);
    };
  }
}
