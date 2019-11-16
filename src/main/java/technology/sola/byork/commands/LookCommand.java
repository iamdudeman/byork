package technology.sola.byork.commands;

import technology.sola.byork.UserInterface;
import technology.sola.byork.gameobjects.GameObject;
import technology.sola.byork.map.ByorkMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class LookCommand implements Command {
  private ByorkMap byorkMap = null;
  private List<Pattern> patterns = null;

  public LookCommand(ByorkMap map) {
    byorkMap = map;

    Command.commands.add(this);

    patterns = new ArrayList<>();
    patterns.add(Pattern.compile("look"));
    patterns.add(Pattern.compile("look at .+"));
  }

  @Override
  public List<Pattern> getPatterns() {
    return patterns;
  }

  @Override
  public String help() {
    return String.format( "%-20s - %s", "look", "observe surroundings" ) + "\n" +
      String.format( "%-20s - %s", "look at [object]", "inspect object" );
  }

  @Override
  public void execute(String command) {
    if (patterns.get(1).matcher(command).matches()) {
      GameObject objectToInspect = null;
      String[] commandParts = command.split(" ");
      Iterator<GameObject> iter = byorkMap.getCurrentLocationGameObjects().iterator();
      while (iter.hasNext()) {
        GameObject currentObject = iter.next();
        if (currentObject.getName().toLowerCase().equals(commandParts[2])) {
          objectToInspect = currentObject;
          break;
        }
      }

      if (objectToInspect != null) {
        UserInterface.getInstance().displayMessage(objectToInspect.inspect());
      } else {
        UserInterface.getInstance().displayErrorMessage("What are you looking at?");
      }
    } else {
      UserInterface.getInstance().displayMessage(byorkMap.getCurrentLocationDescription());
    }
  }

}

