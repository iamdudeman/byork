package technology.sola.byork.gameobjects;

import technology.sola.byork.Player;

public interface GameObject {
  String getId();

  String getName();

  String inspect();

  void use();

  /**
   * This should be called after every movement step of the Player.
   */
  void update();

  void pickup(Player player);

  void drop(Player player);
}
