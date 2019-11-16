package technology.sola.byork.commands;

import technology.sola.byork.Player;
import technology.sola.byork.UserInterface;
import technology.sola.byork.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class DropCommand implements Command {
  private List<Pattern> patterns = null;
  private Player player = null;

  public DropCommand(Player player) {
    this.player = player;

    Command.commands.add(this);

    patterns = new ArrayList<>();
    patterns.add(Pattern.compile("drop .+"));
  }

  @Override
  public List<Pattern> getPatterns() {
    return patterns;
  }

  @Override
  public String help() {
    return String.format("%-20s - %s", "drop [object]", "removes an object to inventory");
  }

  @Override
  public void execute(String command) {
    GameObject objectToDrop = null;
    Iterator<GameObject> iter = player.getInventoryIter();
    String itemName = command.split(" ")[1];
    while (iter.hasNext()) {
      GameObject current = iter.next();
      if (current.getName().toLowerCase().equals(itemName)) {
        objectToDrop = current;
        break;
      }
    }

    if (objectToDrop == null) {
      UserInterface.getInstance().displayErrorMessage("You are not carrying that.");
    } else {
      UserInterface.getInstance().displayMessage("You dropped " + objectToDrop.getName() + ".");
      objectToDrop.drop(player);
    }
  }
}
