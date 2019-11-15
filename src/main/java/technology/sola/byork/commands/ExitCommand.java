package technology.sola.byork.commands;

import technology.sola.byork.Main;
import technology.sola.byork.Player;
import technology.sola.byork.UserInterface;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ExitCommand implements Command {
  private final Player player;

  public ExitCommand(Player player) {
    Command.commands.add(this); // TODO get away from this pattern

    this.player = player;
  }

  @Override
  public List<Pattern> getPatterns() {
    return Collections.singletonList(Pattern.compile("exit"));
  }

  @Override
  public String help() {
    return String.format("%-20s - %s", "exit", "exit the game");
  }

  @Override
  public void execute(String command) {
    UserInterface.getInstance().displayMessage("Thanks for playing!");
    player.stopPlaying();
  }
}
