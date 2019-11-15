package technology.sola.byork;

import technology.sola.byork.commands.CommandInvoker;
import technology.sola.byork.commands.ExitCommand;
import technology.sola.byork.commands.HelpCommand;

public class Main {
  private static final String INTRO_MESSAGE = "Find treasure and escape this dreadful dungeon.";

  public static void main(String[] args) {
    UserInterface userInterface = UserInterface.getInstance();
    CommandInvoker commandInvoker = new CommandInvoker();

    // TODO load map
    Player player = new Player();

    // Create commands
    new HelpCommand();
    new ExitCommand(player);
    // TODO other commands

    userInterface.displayMessage(INTRO_MESSAGE);

    while (player.isPlaying()) {
      userInterface.displayMessage("Command: ");
      String command = userInterface.getInput();

      commandInvoker.invoke(command.trim());
    }

    // TODO display player score
  }
}
