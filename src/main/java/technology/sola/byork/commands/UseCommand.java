package technology.sola.byork.commands;

import technology.sola.byork.Player;
import technology.sola.byork.UserInterface;
import technology.sola.byork.gameobjects.GameObject;
import technology.sola.byork.map.ByorkMap;
import technology.sola.byork.map.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class UseCommand implements Command {
  private ByorkMap _map = null;
  private List<Pattern> _patterns = null;
  private Player _player = null;

  public UseCommand(ByorkMap map, Player player) {
    _map = map;
    _player = player;

    Command.commands.add(this);

    _patterns = new ArrayList<Pattern>();
    _patterns.add(Pattern.compile("use key " + DIRECTIONS));
    _patterns.add(Pattern.compile("use .+"));
  }

  @Override
  public List<Pattern> getPatterns() {
    return _patterns;
  }

  @Override
  public String help() {
    return String.format("%-20s - %s", "use [object]", "use an object") + "\n" +
      String.format("%-20s - %s", "use key [direction]", "use key");
  }

  @Override
  public void execute(String command) {
    String[] commandParts = command.toLowerCase().split(" ");
    switch (commandParts[1]) {
      case "key":
        if (_patterns.get(0).matcher(command.toLowerCase()).matches()) {
          GameObject key = null;
          Iterator<GameObject> iter = _player.getInventoryIter();
          while (iter.hasNext()) {
            GameObject gameObject = iter.next();
            if (gameObject.getName().equals("Key")) {
              key = gameObject;
              break;
            }
          }

          if (key != null) {
            UserInterface.getInstance().displayMessage(Arrays.toString(_map.getDirectionsWithLockedDoorsForCurrentLocation().toArray()));
            UserInterface.getInstance().displayMessage(commandParts[2]);

            if (_map.getDirectionsWithLockedDoorsForCurrentLocation().contains(Direction.parseDirection(commandParts[2]))) {
              _map.setDoorStatusForCurrentLocation(Direction.parseDirection(commandParts[2]), false);
              _player.removeFromInventory(key);
              _map.removeGameObjectFromCurrentLocation(key);
              UserInterface.getInstance().displayMessage("You unlocked the door.");
            } else {
              UserInterface.getInstance().displayErrorMessage("That door is already unlocked");
            }
          } else {
            UserInterface.getInstance().displayErrorMessage("You don't have a key");
          }
        } else {
          UserInterface.getInstance().displayMessage("What are you trying to do with that key?");;
        }
        break;
      case "flashlight":
        Iterator<GameObject> iter = _player.getInventoryIter();
        while (iter.hasNext()) {
          GameObject gameObject = iter.next();
          if (gameObject.getName().equalsIgnoreCase("flashlight")) {
            gameObject.use();
            break;
          }
        }
        break;
      default:
        UserInterface.getInstance().displayErrorMessage("What are you trying to use?");
        break;
    }
  }

}

