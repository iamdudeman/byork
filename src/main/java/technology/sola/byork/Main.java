package technology.sola.byork;

import technology.sola.byork.commands.*;
import technology.sola.byork.map.ByorkMap;
import technology.sola.byork.map.MapUtils;

public class Main {
  private static final String INTRO_MESSAGE = "Find treasure and escape this dreadful dungeon.";
  private static Player player;

  public static void main(String[] args) {
    UserInterface userInterface = UserInterface.getInstance();
    CommandInvoker commandInvoker = new CommandInvoker();

    ByorkMap byorkMap = MapUtils.loadMap("resources/first_dungeon.json");
    player = new Player(byorkMap);

    // Create commands
    new DropCommand(player);
    new ExitCommand(player);
    new HelpCommand();
    new InventoryCommand(player);
    new LookCommand(byorkMap);
    new MoveCommand(player);
    new PickupCommand(byorkMap, player);
    new UseCommand(byorkMap, player);

    userInterface.displayMessage(INTRO_MESSAGE);

    while (player.isPlaying()) {
      userInterface.displayMessage("Command: ");
      String command = userInterface.getInput();

      commandInvoker.invoke(command.trim());
    }

    UserInterface.getInstance().displayMessage("Your score: " + player.getScore());
  }

  public static Player getPlayer() {
    return player;
  }
}
