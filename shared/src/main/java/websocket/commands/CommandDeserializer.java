package websocket.commands;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CommandDeserializer implements JsonDeserializer<UserGameCommand> {
  @Override
  public UserGameCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject=jsonElement.getAsJsonObject();

    String commandTypeString=jsonObject.get("commandType").getAsString();
    UserGameCommand.CommandType commandType=UserGameCommand.CommandType.valueOf(commandTypeString);

    return switch (commandType) {
      case CONNECT -> context.deserialize(jsonElement, ConnectCommand.class);
      case MAKE_MOVE -> context.deserialize(jsonElement, MakeMoveCommand.class);
      case LEAVE -> context.deserialize(jsonElement, LeaveGameCommand.class);
      case RESIGN -> context.deserialize(jsonElement, ResignCommand.class);
    };
  }
}

