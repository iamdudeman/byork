package technology.sola.byork.commands;

import technology.sola.byork.UserInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HelpCommand implements Command {
  private List<Pattern> patterns = new ArrayList<>();

  public HelpCommand() {
    Command.commands.add(this); // TODO get away from this pattern

    patterns.add(Pattern.compile("help"));
  }

  @Override
  public List<Pattern> getPatterns() {
    return patterns;
  }

  @Override
  public String help() {
    return String.format("%-20s - %s", "help", "display possible commands");
  }

  @Override
  public void execute(String command) {
    commands.forEach(action -> UserInterface.getInstance().displayMessage(action.help()));
  }
}

