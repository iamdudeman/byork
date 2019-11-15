package technology.sola.byork.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public interface Command {
  // TODO shouldn't be constant here
  String DIRECTIONS = "(north|south|east|west|right|left|up|down)";

  // TODO shouldn't be declared here
  List<Command> commands = new ArrayList<>();

  List<Pattern> getPatterns();

  String help();

  void execute(String command);
}
