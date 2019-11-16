package technology.sola.byork.commands;

import technology.sola.byork.Player;
import technology.sola.byork.UserInterface;
import technology.sola.byork.map.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MoveCommand implements Command {
  private List<Pattern> patterns = null;
  private Player player = null;

  public MoveCommand(Player player) {
    this.player = player;

    Command.commands.add(this);

    patterns = new ArrayList<>();
    patterns.add(Pattern.compile("move " + DIRECTIONS));
  }

  @Override
  public List<Pattern> getPatterns() {
    return patterns;
  }

  @Override
  public String help() {
    return String.format("%-20s - %s", "move [dir]", "attempt moving in a direction");
  }

  @Override
  public void execute(String command) {
    String event = null;
    String direction = command.split(" ")[1];
    event = player.move(Direction.parseDirection(direction));
    UserInterface.getInstance().displayMessage(event);
  }
}

