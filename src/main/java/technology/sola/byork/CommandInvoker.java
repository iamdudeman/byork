package technology.sola.byork;

import technology.sola.byork.UserInterface;
import technology.sola.byork.commands.Command;

import java.util.Iterator;
import java.util.regex.Pattern;

public class CommandInvoker {
  public void invoke(String input) {
    Command command = parse(input.toLowerCase());

    if (command != null) {
      command.execute(input.toLowerCase());
    } else {
      UserInterface.getInstance().displayErrorMessage("Not a command");
    }
  }

  private Command parse(String input) {
    Iterator<Command> iterCommand = Command.commands.iterator();

    // TODO clean up with Streams
    while (iterCommand.hasNext()) {
      Command currentCommand = iterCommand.next();
      Iterator<Pattern> iterPatterns = currentCommand.getPatterns().iterator();

      while (iterPatterns.hasNext()) {
        Pattern currentPattern = iterPatterns.next();

        if (currentPattern.matcher(input).matches()) {
          return currentCommand;
        }
      }
    }

    return null;
  }
}

