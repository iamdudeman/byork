package technology.sola.byork.gameobjects;

import technology.sola.byork.Player;
import technology.sola.byork.map.ByorkMap;

public class Key implements GameObject {
  private static int keyCount = 0;
  private final String id;
  private ByorkMap byorkMap;
  private Player player = null;

  /**
   * Builds this Key object. It is initialized with a reference to the
   * ByorkMap object so it can know what doors are locked or unlocked.
   *
   * @param byorkMap The ByorkMap object
   */
  public Key(ByorkMap byorkMap) {
    this.byorkMap = byorkMap;

    keyCount++;

    id = "Key" + keyCount;
  }

  public void setZorkMap(ByorkMap byorkMap) {
    this.byorkMap = byorkMap;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return "Key";
  }

  @Override
  public String inspect() {
    return "A key to a locked door.";
  }

  @Override
  public void use() {
    // TODO figure out how to put direction in there
    byorkMap.setDoorStatusForCurrentLocation(null, false);
    drop(player);
  }

  @Override
  public void update() {
    // Nothing to do here
  }

  @Override
  public void pickup(Player player) {
    player.addToInventory(this);
  }

  @Override
  public void drop(Player player) {
    player.removeFromInventory(this);
  }
}
