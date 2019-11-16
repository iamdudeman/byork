package technology.sola.byork.map;

import technology.sola.byork.gameobjects.GameObject;

import java.util.List;

public interface ByorkMap {
  String move(Direction direction);

  String getCurrentLocationDescription();

  /**
   * Method for getting a List of all of the GameObject objects that are found
   * in the player's current Location.
   *
   * @return The List of GameObject
   */
  List<GameObject> getCurrentLocationGameObjects();

  /**
   * Method for adding a GameObject to the Location the player is currently
   * in.
   *
   * @param gameObject The GameObject to add
   */
  void addGameObjectToCurrentLocation(GameObject gameObject);

  /**
   * Method for removing a GameObject from the Location the player is
   * currently in.
   *
   * @param gameObject The GameObject to remove
   */
  void removeGameObjectFromCurrentLocation(GameObject gameObject);

  /**
   * Method that returns a List of all of the directions for movement in the
   * current Location where a locked door can be found.
   *
   * @return The List of Direction with locked doors
   */
  List<Direction> getDirectionsWithLockedDoorsForCurrentLocation();

  /**
   * Method that sets a door as locked or unlocked for a given Direction.
   *
   * @param direction The Direction the door is at
   * @param isLocked  Whether the door should be locked or not, so when true
   *                  the door becomes locked, and when false the door becomes
   *                  unlocked.
   */
  void setDoorStatusForCurrentLocation(Direction direction, boolean isLocked);

  /**
   * Method that adds a GameObject to a target Location.
   *
   * @param locationId The id of the target Location
   * @param gameObject The GameObject to be added
   */
  void addGameObjectToLocation(String locationId, GameObject gameObject);

  /**
   * Method that can retrieve an Location object from its location id
   *
   * @param locationId The id of the Location desired.
   * @return a Location object.
   */
  Location retrieveAnyLocation(String locationId);

  /**
   * Method that can place a given Location object at a given location id.
   *
   * @param locationId      the id of the location where the given location object
   *                        will be placed.
   * @param aLocationObject the given location object to be placed.
   */
  void replaceAnyLocation(String locationId, Location aLocationObject);

  boolean isCurrentLocationExit();
}
