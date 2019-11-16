package technology.sola.byork.map;

import technology.sola.byork.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds all the essential items about some point on the map, such as location ID (which is some node on the map);
 * location links (which tells what N,E,S,W respectively); location connection types describing whether relative
 * direction has open, closed or undefined paths; and a location description. Current game objects at this point
 * on the map are also stored here.
 *
 * @author Brint
 *
 * @version 4/8/2015
 *
 */
public class Location {
  private static final String CR_LF = "\r\n";

  /**
   * ID number of the map node for this room.
   */
  private int locationID;

  /**
   * Contains 4-tuple showing node id's to the NORTH, EAST, SOUTH, WEST, UP, DOWN respectively.  Example: [ U,2,3,U,U,U ]
   */
  private String[] locationLinks;

  /**
   * Contains 4-tuple showing the type of connection (open, closed, undefined) to
   * the NORTH, EAST, SOUTH, WEST, UP, DOWN respectively. Example: [U,U,C,O,U,U]
   */
  private String[] locationConnTypes;

  /**
   * Contains 4-tuple showing the type of direction description (open, closed, undefined, custom) to
   * the NORTH, EAST, SOUTH, WEST, UP, DOWN respectively. Example: [U,O,"a screen door",U,"an attic entry",U]
   *
   */
  private String[] locationDirecDescs;

  /**
   * A description of the current location
   */
  private String 	 locationDesc;

  /**
   * The list of object that are at this location.
   */
  private List<GameObject> currentLocationGameObjects;

  /**
   * When true, only a generic description for a dark room is returned. (e.g. "A dark room.")
   * The default setting is false, so this must be explicitly set to true for the generic description
   * to be given.
   */
  private boolean isDark = false;

  /**
   * When true this location will be considered an exit and the game will end when the player moves
   * here.
   */
  private boolean isExit = false;


  /**
   * Naked Constructor
   */
  public Location() { }

  /**
   * Constructor
   *
   * @param loc	an integer location id (i.e. node of map)
   * @param links a String 6-tuple describing what location id is North, East, South, West, Up, or Down respectively.
   * @param types a String 6-tuple describing what type of connection is available, such as Open, Closed, or undefined (e.g. "U,O,C,U,U,U")
   * @param desc a String description of the location.
   */
  public Location(String loc, String links, String types, String dirdescs, String desc) {

    this.currentLocationGameObjects = new ArrayList<>(); // be prepared to hold stuff placed in room.
    this.locationID 		= MapUtils.strToInt(loc);
    this.locationLinks 		= this.extractLocationLinks(links);
    this.locationConnTypes 	= this.extractConnTypes(types);
    this.locationDirecDescs = this.extractDirecDescs(dirdescs);
    this.locationDesc 		= this.extractDescription(desc);


  }


  /**
   * Show everything you know
   */
  public String toString() {

    String str = "";

    str += "locationID.......... " + this.locationID + CR_LF;
    str += "isDark.............  " + this.isDark + CR_LF;
    str += "locationLinks....... " + Arrays.toString(this.locationLinks) + CR_LF;
    str += "locationConnTypes... " + Arrays.toString(this.locationConnTypes) + CR_LF;
    str += "locationDirecDescs.. " + Arrays.toString(this.locationDirecDescs) + CR_LF;
    str += "locationDesc:....... " + this.locationDesc + CR_LF;
    str += this.hasObjectsIn(locationID) + CR_LF;

    return str;

  }

  /**
   * Extraction the 6-tuple locations string into an array with four elements.
   *
   * @param str
   *            a 6-tuple locations string.
   * @return an array with the location links in respective array element
   *         order.
   */
  private String[] extractLocationLinks (String str) {
    return MapUtils.csvToArray(str);
  }

  /**
   * Extraction the 6-tuple connections type string into an array with six elements.
   *
   * @param str
   *            a 6-tuple connections type string.
   * @return an array with the connection types in respective array element
   *         order.
   */
  private String[] extractConnTypes (String str) {
    return MapUtils.csvToArray(str);
  }


  /**
   * Extraction the 6-tuple direction description type string into an array with six elements.
   *
   * @param str
   *            a 6-tuple direction description type string.
   * @return an array with the direction description types in respective array element
   *         order.
   */
  private String[] extractDirecDescs(String str) {
    return MapUtils.csvToArray(str);
  }


  /**
   * At this point, simply loads the class attribute from the supplied string.
   * Later, it might have to examine or process something in the description,
   * thus why it gets it's own method.
   *
   * @param str
   *            a given description of the location.
   * @return void
   */
  public String extractDescription (String str) {

    locationDesc = str;

    return locationDesc;

  }

  /**
   * Given a room location, list the game objects in that location.
   *
   * @param roomId
   *            a room id
   *
   * @return a description of the objects in the room, or "none" if there are no objects.
   */
  public String hasObjectsIn( int roomId) {

    String str = "These objects are in the room:" + CR_LF;

    try {

      if (this.currentLocationGameObjects.isEmpty()) str+= "  none";

      for (int i = 0; i < this.currentLocationGameObjects.size(); i++) {
        str += "  " + this.currentLocationGameObjects.get(i).getName().toString() + CR_LF;
      }
    }
    catch (Exception e){ // if no object array created

      str += "  none";

    }



    return str;
  }
  /**
   * Given a direction, reveal what location id (i.e. node on the
   * map) is at that direction; but, if no direction defined from
   * the location, return a -1.
   *
   * @param direction a direction enum element (NORTH, SOUTH, EAST, WEST)
   * @return an integer representing a location id, or a -1 if undefined.
   */
  public int hasLocationAt (Direction direction) {

    int locId = -999;
    int ordDir = direction.ordinal(); // get ordinal index of direction [ ordDir ]

    try
    {
      locId = MapUtils.strToInt( locationLinks[ordDir] ); // get loc. id [locId] at direction from locationLinks[ordDir]
    }
    catch (Exception e) // a "U" found.
    {
      locId = -1;
    }

    return locId;

  }

  /**
   * Given a direction, reveal what connection type is there (i.e. open, closed, undefined, etc.)
   * is at that direction;
   *
   * @param direction a direction enum element (NORTH, SOUTH, EAST, WEST, UP, DOWN)
   * @return an String character representing a connection type -- i.e., "O","C","U"
   */
  public String hasConnectionTypeAt (Direction direction) {

    String connType = "";
    int ordDir = direction.ordinal(); // get ordinal index of direction [ ordDir ]

    connType = this.locationConnTypes[ordDir]; // get loc. id [locId] at direction from locationLinks[ordDir]

    return connType;

  }

  /**
   * Given a direction, if there is a custom description for that direction
   * per this room, return that description, otherwise return an empty string
   * (e.g. "")
   *
   * @param direction
   *            -- a direction enum element (NORTH, SOUTH, EAST, WEST, UP,
   *            DOWN)
   * @return a custom description, or empty string if none.
   */
  public String hasDirectionDescriptionAt (Direction direction) {

    String dirDesc = "";
    int ordDir = direction.ordinal();

    dirDesc = this.locationDirecDescs[ordDir];  // e.g. "a stairway"

    if (dirDesc.length() == 1) { // if a U,O, or C character.
      dirDesc = ""; } // no custom description, so plan to return an empty string.
    else {
      dirDesc = direction + " is " + dirDesc + "."; // custom, so build it. e.g. SOUTH is a stairway
    }

    return dirDesc;
  }

  /**
   * Returns a simple description of what kind of path is at the given direction (details).
   * <pre>
   * Examples:
   *   There is [a locked door] to the SOUTH.
   *   There is [an open path] to the EAST.
   *   </pre>
   *
   * @return a string showing a simple direction description, or empty string ("") if there is no path defined
   * at the given direction.
   */
  public String lookOneDirectionDescription(Direction direction) {

    final String NO_CUSTOM_DESCRIPTION = "";

    String str = hasDirectionDescriptionAt(direction);

    if ( str.equals(NO_CUSTOM_DESCRIPTION) ) {
      str = giveGenericDirectionDescription(direction);
    }


    return str;
  }

  /**
   * Returns a simple description of what kind of path is at the given direction (details).
   * <pre>
   * Examples:
   *   There is [a locked door] to the SOUTH.
   *   There is [an open path] to the EAST.
   *   </pre>
   *
   * @param direction  -- a direction enum element (NORTH, SOUTH, EAST, WEST, UP,
   *            DOWN)
   * @return
   */
  private String giveGenericDirectionDescription(Direction direction) {

    String str = "";
    String simpleDesc = "";
    String directionDesc = "";

    /* describe path type */
    switch (hasConnectionTypeAt (direction)) {
      case "O":
        simpleDesc = "an open path ";
        break;
      case "C":
        simpleDesc = "a locked door ";
        break;
      case "U":
        //return ""; // nothing connected, so return empty string immediately.
        break; // do nothing.
      default:
        throw new IllegalArgumentException("ERROR: Unknown conn. type:" + hasConnectionTypeAt (direction));

    }

    /* build linking description */
    switch (simpleDesc) {
      case "an open path ":
        directionDesc = "going ";
        break;
      case "a locked door ":
        directionDesc = "to the ";
        break;
      case "":
        break; // do nothing.
      default:
        throw new IllegalArgumentException("ERROR: Unknown simple desc.:" + simpleDesc);
    }

    if (!simpleDesc.equals("")) { // if something to be noted...
      /* add direction */
      directionDesc+= direction.toString();
      str = "There is " + simpleDesc + directionDesc;
    }
    return str;
  }

  /**
   * Describe what is at all directions from the player location
   *
   * @return A formatted string containing simple descriptions of what the
   *         player sees in all directions
   */
  public String lookAllDirectionDescriptions() {

    Direction direction;
    String oneDirDesc = "";
    String allDirDescs = "";

    for (int i = 0; i < 6; i++) { // look at all 6 directions
      direction = Direction.values()[i]; // get the right direction to examine
      oneDirDesc = lookOneDirectionDescription(direction);  // get any desc. for that direction.
      if (!oneDirDesc.equals("")) { // if non empty description...
        allDirDescs += lookOneDirectionDescription(direction) + CR_LF; // add it in.
      } // end if
    } // end for

    return allDirDescs;
  }

  /**
   * Add a game object and return how many total objects are in the current
   * location.
   *
   * @param obj
   *            a Game object such as a key, flash light, etc.
   * @return an int showing how many total objects are in the current location
   */
  public int addGameObject( GameObject obj) {

    this.currentLocationGameObjects.add(obj);

    return this.currentLocationGameObjects.size();

  }

  /**
   * Remove the game object and return how many total objects are in the current
   * location
   *
   * @param obj
   *            a Game object such as a key, flash light, etc.
   *
   * @return true if the object was in the location, false otherwise.
   */
  public boolean removeGameObject( GameObject obj) {

    return this.currentLocationGameObjects.remove(obj);

  }

  /**
   * Returns a count of how many game objects are at the current location.
   *
   * @return an int of how many game objects are at the current location
   */
  public int countGameObjects() {

    return this.currentLocationGameObjects.size();
  }

  /**
   * Given a direction, return the polar opposite direction, so NORTH yields
   * SOUTH, EAST yields WEST, etc.
   *
   * @return a direction opposite of what was requested.
   */
  public Direction calcOppositeDirection(Direction direction) {

    int i = direction.ordinal();
    //Enum of Direction = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, UP, DOWN };
    Direction[] output  = { Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST, Direction.DOWN, Direction.UP };

    return output[i];
  }

  // -----------------------
  // getters & setters
  // -----------------------

  public int getLocationID() {
    return locationID;
  }

  public void setLocationID(int locationID) {
    this.locationID = locationID;
  }

  public String[] getLocationLinks() {
    return locationLinks;
  }

  public void setLocationLinks(String[] locationLinks) {
    this.locationLinks = locationLinks;
  }

  public String[] getLocationConnTypes() {
    return locationConnTypes;
  }

  public void setLocationConnTypes(String[] locationConnTypes) {
    this.locationConnTypes = locationConnTypes;
  }

  public String getLocationDesc() {
    return locationDesc;
  }

  public void setLocationDesc(String locationDesc) {
    this.locationDesc = locationDesc;
  }

  public List<GameObject> getCurrentLocationGameObjects() {
    return currentLocationGameObjects;
  }

  public void setCurrentLocationGameObjects(
    List<GameObject> currentLocationGameObjects) {
    this.currentLocationGameObjects = currentLocationGameObjects;
  }

  public boolean getIsDark() {
    return isDark;
  }

  public void setIsDark(boolean isDark) {
    this.isDark = isDark;
  }

  public boolean getIsExit() {
    return isExit;
  }

  public void setIsExit(boolean isExit) {
    this.isExit = isExit;
  }

  public String[] getLocationDirecDescs() {
    return locationDirecDescs;
  }

  public void setLocationDirecDescs(String[] locationDirecDescs) {
    this.locationDirecDescs = locationDirecDescs;
  }
}
