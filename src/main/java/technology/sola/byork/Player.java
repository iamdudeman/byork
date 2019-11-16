package technology.sola.byork;

import technology.sola.byork.gameobjects.GameObject;
import technology.sola.byork.map.ByorkMap;
import technology.sola.byork.map.Direction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Player {
  private boolean isPlaying = true;
  private List<GameObject> inventory;
  private ByorkMap map;
  private boolean canSeeInDark = false;
  private int score = 0;

  public Player(ByorkMap map) {
    this.map = map;
    inventory = new LinkedList<>();
  }

  public void addToScore(int score) {
    this.score += score;
  }

  public int getScore() {
    return score;
  }

  public void addToInventory(GameObject gameObject) {
    inventory.add(gameObject);
    map.removeGameObjectFromCurrentLocation(gameObject);
  }

  public void removeFromInventory(GameObject gameObject) {
    inventory.remove(gameObject);
    map.addGameObjectToCurrentLocation(gameObject);
  }

  public Iterator<GameObject> getInventoryIter() {
    return inventory.iterator();
  }

  public void stopPlaying() {
    isPlaying = false;
  }

  boolean isPlaying() {
    return isPlaying;
  }

  public boolean canSeeInDark() {
    return canSeeInDark;
  }

  public void setCanSeeInDark(boolean canSeeInDark) {
    this.canSeeInDark = canSeeInDark;
  }

  public String move(Direction dir) {
    String moveMessage;

    if (map.getCurrentLocationDescription().contains("A dark room.")) {
      moveMessage = "You were eaten by a Grue.";
      stopPlaying();
    } else {
      moveMessage = map.move(dir);
      if (moveMessage.contains("completed")) {
        inventory.forEach(GameObject::update);
      }

      if (map.isCurrentLocationExit()) {
        moveMessage = "You have found the exit.";
        stopPlaying();
      }
    }

    return moveMessage;
  }
}
