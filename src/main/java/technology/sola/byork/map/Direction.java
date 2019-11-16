package technology.sola.byork.map;

public enum Direction {
  NORTH,
  EAST,
  SOUTH,
  WEST,
  UP,
  DOWN;

  public static Direction parseDirection(String direction) {
    switch (direction.toLowerCase()) {
      case "north":
        return NORTH;
      case "east":
      case "right":
        return EAST;
      case "south":
        return SOUTH;
      case "west":
      case "left":
        return WEST;
      case "up":
        return UP;
      case "down":
        return DOWN;
      default:
        throw new IllegalArgumentException("No direction known for key [" + direction + "]");
    }
  }
}

