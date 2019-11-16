package technology.sola.byork.commands;

import technology.sola.byork.Player;
import technology.sola.byork.UserInterface;
import technology.sola.byork.gameobjects.GameObject;
import technology.sola.byork.map.ByorkMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class PickupCommand implements Command {
  private List<Pattern> patterns = null;
  private Player player = null;
  private ByorkMap byorkMap = null;

  public PickupCommand(ByorkMap zorkMap, Player player) {
    byorkMap = zorkMap;
    this.player = player;

    Command.commands.add(this);

    patterns = new ArrayList<>();
    patterns.add(Pattern.compile("(pickup|take) .+"));
  }

  @Override
  public List<Pattern> getPatterns() {
    return patterns;
  }

  @Override
  public String help() {
    return String.format("%-20s - %s", "pickup [object]", "adds an object to inventory");
  }

  @Override
  public void execute(String command) {
    GameObject objectToPickup = null;
    Iterator<GameObject> iter = byorkMap.getCurrentLocationGameObjects().iterator();
    String itemName = command.split(" ")[1];
    while (iter.hasNext()) {
      GameObject current = iter.next();
      if (current.getName().toLowerCase().equals(itemName)) {
        objectToPickup = current;
        break;
      }
    }

    if (objectToPickup == null) {
      UserInterface.getInstance().displayErrorMessage("That object is not here.");
    } else {
      if (!objectToPickup.getName().equals("Scroll") && !objectToPickup.getName().equals("Treasure")) {
        UserInterface.getInstance().displayMessage("You picked up " + objectToPickup.getName() + ".");
      }
      objectToPickup.pickup(player);
    }
  }
}

