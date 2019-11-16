package technology.sola.byork.commands;

import technology.sola.byork.Player;
import technology.sola.byork.UserInterface;
import technology.sola.byork.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class InventoryCommand implements Command {
  private List<Pattern> patterns = null;
  private Player player = null;

  public InventoryCommand(Player player) {
    this.player = player;

    Command.commands.add(this);

    patterns = new ArrayList<>();
    patterns.add(Pattern.compile("inventory"));
    patterns.add(Pattern.compile("^i$"));
  }

  @Override
  public List<Pattern> getPatterns() {
    return patterns;
  }

  @Override
  public String help() {
    return String.format("%-20s - %s", "inventory", "display current inventory");
  }

  @Override
  public void execute(String command) {
    int itemCount = 0;
    Iterator<GameObject> inventoryIter = player.getInventoryIter();
    while (inventoryIter.hasNext()) {
      UserInterface.getInstance().displayMessage(inventoryIter.next().getName());
      itemCount++;
    }

    if (itemCount == 0) {
      UserInterface.getInstance().displayMessage("You have nothing");
    }
  }
}

