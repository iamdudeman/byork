package technology.sola.byork.map.impl;

import technology.sola.byork.Main;
import technology.sola.byork.gameobjects.GameObject;
import technology.sola.byork.map.ByorkMap;
import technology.sola.byork.map.Direction;
import technology.sola.byork.map.Location;
import technology.sola.byork.map.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ByorkMapImpl implements ByorkMap {
  private static final String PATH_CLOSED    = "C";
  private static final String PATH_OPEN      = "O";
  private static final String PATH_UNDEFINED = "U";
  private static final String CR_LF = "\r\n";


  /**
   * The key is a room location, and the value is a 4-tuple showing what room
   * is NORTH, EAST, SOUTH, WEST, UP or DOWN respectively.  If there is no room connection
   * linked to another room in the direction, then a "U" means undefined.<p>
   * Example:
   * <pre>
   0=U,1,2,U,5,U
   1=U,U,U,0,U,U
   2=0,3,U,U,U,U
   3=U,U,4,2,U,U
   4=3,U,U,U,U,U
   5=U,U,U,U,U,0
   * </pre>
   */
  private Map<String,String> locToLinksHM;


  /**
   * The key is a room location, the value is a 4-tuple:
   *
   * <pre>
   * Legend:
   * U = UNDEFINED
   * O = PATH_OPEN
   * C = PATH_CLOSED
   * Example:
   *
   * 0=U,O,O,U,O,U
   * 1=U,U,U,O,U,U
   * 2=O,O,U,U,U,U
   * 3=U,U,C,O,U,U
   * 4=C,U,U,U,U,U
   * 5=U,U,U,U,U,O
   * </pre>
   *
   */
  private Map<String,String> locToConnHM;


  /**
   * <pre>
   * location=N desc,E desc,S desc,W desc, U desc, D desc
   * e.g.: "SOUTH is a screen door"
   * e.g.: "UP is an attic entry"
   * e.g.: "DOWN is a stairway"
   * "U" gives only generic direction descriptions
   *
   * Example:
   * 0=U,U,"a screen door",U,"an attic entry",U
   * 1=U,U,U,U,U,U
   * 2=U,U,U,U,U,U
   * 3=U,U,U,U,U,U
   * 4=U,U,U,U,U,U
   * 5=U,U,U,U,U,"a stairway"
   * </pre>
   */
  private Map<String,String> locToDirecDescHM;


  /**
   * The key is a room location, the value is a room description.
   * <pre>
   * Example:
   *
   * 0="0 is in a house."
   * 1="1 is in front yard."
   * 2="2 is in back yard."
   * 3="3 is in a shed."
   * 4="4 is in a cellar."
   * 5="5 is in the attic."
   *</pre>
   */
  private Map<String, String> locToDesHM;


  /**
   * Keep all rooms persistent objects so that game objects and room changes stay persistent
   * as player comes and goes to rooms.
   */
  private Map<String, Location> allRooms;


  /**
   * Contains all the information about the node where the player is located.
   */
  private Location playerLocation;


  /**
   * Describes what conditions obtain after the most recent moved attempt.<p>
   * Format: "[link status],[status description]"
   * <pre>
   * Examples:
   * "3,SOUTH path movement completed and open. // shows which link player came from, and which direction the player moved.
   * "U,NORTH path unavailable" // shows undefined link connection, and which direction player attempted.
   * "2,EAST path movement closed"  // shows which link at the attempted direction is connected (but closed), and which direction player attempted.
   * </pre>
   */
  private String moveAttemptStatus;


  public ByorkMapImpl(
    String startingLocId,
    Map<String,	String> locToLinksHM2,
    Map<String,	String> locToConnHM2,
    Map<String,	String> locToDirDesc,
    Map<String, String> locToDesHM2)
  {

    this.locToLinksHM = locToLinksHM2;
    this.locToConnHM  = locToConnHM2;
    this.locToDirecDescHM = locToDirDesc;
    this.locToDesHM   = locToDesHM2;
    this.allRooms = new HashMap<>();

    String roomId = "";
    for (int i = 0; i < locToLinksHM.size(); i++) {

      roomId = MapUtils.intToStr( i ); // hash map needs string keys.
      Location temp = new Location(		// create the room
        roomId,
        locToLinksHM.get(roomId),
        locToConnHM.get(roomId),
        locToDirecDescHM.get(roomId),
        locToDesHM.get(roomId)
      );

      this.allRooms.put(roomId, temp);  // store it persistently as <key, value>

    }

    this.playerLocation = this.allRooms.get(startingLocId);
  }

  /**
   * Show everything you know
   */
  public String toString() {

    String str = "";

    str += "locToLinksHM...... " + this.locToLinksHM.toString() + CR_LF;
    str += "locToConnHM....... " + this.locToConnHM.toString() + CR_LF;
    str += "locToDirecDescHM.. " + this.locToDirecDescHM.toString() + CR_LF;
    str += "locToDesHM........ " + this.locToDesHM.toString() + CR_LF;
    str += "allRooms size..... " + this.allRooms.size() + CR_LF;
    str += "playerLocation:    " + "\r\n"+ this.playerLocation.toString() + CR_LF;

    return str;

  }

  /**
   * Given a direction and a fully traversable map, move the player in the
   * direction indicated and update all player info, but if move cannot be
   * made, leave all player location info as is.
   *
   * @param direction
   *            an enum of NORTH, EAST, SOUTH, or WEST.
   * @return String which describes what conditions obtain after the most
   *         recent moved attempt.
   *         <p>
   *         Format: "[link status],[status description]"
   *
   *         <pre>
   * Examples:
   * "3,SOUTH path movement completed and open. // shows which link player came from, and which direction the player moved.
   * "U,NORTH path unavailable" // shows undefined link connection, and which direction player attempted.
   * "2,EAST path movement closed"  // shows which link at the attempted direction is connected (but closed), and which direction player attempted.
   * </pre>
   */
  public String move( Direction direction )
  {

    String newLocId = "";
    String connType = playerLocation.hasConnectionTypeAt(direction); // look in the direction for connection type [connType:String]


    switch (connType) {
      case "O":
        newLocId = MapUtils.intToStr(playerLocation.hasLocationAt(direction)); // get new loc. link id. [newLocId]

        /* show where we just left, and which way we just traveled. */
        moveAttemptStatus = playerLocation.getLocationID()+","+direction.toString()+" path movement completed and open";

        this.playerLocation = this.allRooms.get(newLocId);

        break;
      case "U":
        moveAttemptStatus = connType+","+direction.toString()+" path undefined";
        break;
      case "C":
        moveAttemptStatus = playerLocation.hasLocationAt(direction)+","+direction.toString()+" path closed.";
        break;
      default:
        throw new IllegalArgumentException("ERROR: Unrecognized connection type: " + connType);
    }

    return moveAttemptStatus;
  }

  /**
   * Returns the description of the player's current location on the map, unless
   * location is set to be dark, which then returns a generic "A dark room."
   */
  public String getCurrentLocationDescription()
  {

    String locationDescription  = "";
    String directionDescriptions = "";
    String roomObjects          = "";
    String fullDescription      = "";

    if (!playerLocation.getIsDark() || Main.getPlayer().canSeeInDark()) {

      locationDescription = playerLocation.getLocationDesc() + CR_LF;
      directionDescriptions = playerLocation.lookAllDirectionDescriptions();
      int roomId = playerLocation.getLocationID();
      roomObjects = playerLocation.hasObjectsIn(roomId);
      fullDescription = locationDescription + directionDescriptions + roomObjects;
    }
    else {

      fullDescription = "A dark room.";
    }

    return fullDescription + "\r\n";
  } // end method


  public List<Direction> getDirectionsWithLockedDoorsForCurrentLocation() {

    Direction[] directions 		 	=  Direction.values(); // quick load an array.
    List<Direction> dirList 	= new ArrayList<>();
    Direction lookDirection		 	= null;
    Direction invLookDirection 	 	= null;
    int 	  targetRoom 		 	= -999;
    String 	  connTypeAtOrg 		= null;
    String 	  invConnTypeAtTarg 	= null;
    boolean	  lockedDoor		 	= false;
    Location  tempLoc 			 	= null;

    for (int ptrDir=0; ptrDir < 6; ptrDir++) {// look in all 6 directions from origin e.g. 0,1,2,3 = N,E,S,W,U,D respectively.
      lookDirection = directions[ptrDir];// grab this location node direction. [lookDirection] e.g. NORTH (of origin node)
      connTypeAtOrg = playerLocation.hasConnectionTypeAt(lookDirection);// grab origin location node connection type [nodeConn] e.g. C
      targetRoom = playerLocation.hasLocationAt(lookDirection);// grab link at direction [linkDir] e.g. 3
      if (targetRoom >= 0) { // if there's a link to worry about in this direction.
        lockedDoor = isDoorStatus(lookDirection, targetRoom, connTypeAtOrg);
        if (lockedDoor) {// if it's locked
          dirList.add(lookDirection);// add target node, direction to dirList
        } // end if
      } // end if
    }// end loop
    return dirList;
  }

  /**
   * From the origin room of the player's location, determine if a door at a given
   * direction is locked or not.
   *
   * @param lookDirection
   *            a direction (e.g.  NORTH, EAST, SOUTH, or WEST )
   * @param targetRoom
   *            a room location on the map at the look direction (e.g. 3)
   * @param connTypeAtOrg
   *            the connection type for the direction within the room where the player is currently
   *            located. (e.g. "C")
   *
   * @return a true if the door is locked at the look direction, false
   *         otherwise
   */
  public boolean isDoorStatus(Direction lookDirection, int targetRoom, String connTypeAtOrg) {

    Direction invLookDirection;
    String invConnAtTarg;
    boolean orginRoomConnClosed;
    boolean targetRoomConnClosed;
    boolean doorStatus;
    Location tempLoc;

    tempLoc = this.allRooms.get(targetRoom+"");
    invLookDirection = playerLocation.calcOppositeDirection(lookDirection); /* get opp. dirctn looking back toward origin from target e.g. SOUTH */
    invConnAtTarg = tempLoc.hasConnectionTypeAt(invLookDirection);// grab connection type at inverse of origin direction. e.g. C
    orginRoomConnClosed = connTypeAtOrg.equals(PATH_CLOSED);	   // is it closed from player side
    targetRoomConnClosed = invConnAtTarg.equals(PATH_CLOSED); // and other side too? (and derive proper direction to look from target)
    doorStatus = orginRoomConnClosed && targetRoomConnClosed; // both being closed is eqiv. to a locked door.

    return doorStatus;
  }


  @Override
  public void setDoorStatusForCurrentLocation(Direction direction,
                                              boolean isLocked) {

    Direction[] directions =  Direction.values(); // quick load an array.

    String[] orgConnTupleArray = playerLocation.getLocationConnTypes(); // pull hashmap 4-tuple connection type [OrgConnTuple]  e.g. "U,U,C,O"

    /* set origin room to open(false)/locked(true) based on ordinal value of direction */
    int iDirection = direction.ordinal(); /// e.g. int foo = direction.ordinal() = 2
    orgConnTupleArray[iDirection] = (isLocked) ? "C" : "O"; /// change the status of this side of the door as requested
    playerLocation.setLocationConnTypes(orgConnTupleArray);// put it back into origin room for player location { by ref. means might not need this. }
    String a4tuple = MapUtils.arrayToCSV(playerLocation.getLocationConnTypes()); // e.g. "[U][U][C][O]" to "U,U,C,O"
    this.locToConnHM.put(playerLocation.getLocationID()+"", a4tuple);// put it back into connection hashmap to make it perminent

    // derive target room location
    int targetRoom = playerLocation.hasLocationAt(direction);// grab link at direction [linkDir] e.g. 3

    Location tempLoc = this.allRooms.get(targetRoom+""); // get the target room.

    String[] targConnTupleArray = tempLoc.getLocationConnTypes(); // pull hashmap 4-tuple connection type [OrgConnTuple]  e.g. "C,U,U,U"
    Direction invLookDirection = playerLocation.calcOppositeDirection(direction); /* get opp. dirctn looking back toward origin from target e.g. SOUTH */ // derive inverse direction [invConnAtTarg]
    // set target room to open(false)/locked(true) based on ordinal value of inverse direction
    targConnTupleArray[invLookDirection.ordinal()] = (isLocked) ? "C" : "O"; /// change the status of target side of the door too per request
    tempLoc.setLocationConnTypes(targConnTupleArray);// put it back into target room for target location {don't need this}
    a4tuple = MapUtils.arrayToCSV(tempLoc.getLocationConnTypes()); // e.g. "[O][U][U][U]" to "O,U,U,U"
    this.locToConnHM.put(tempLoc.getLocationID()+"", a4tuple);// put it back into connection hashmap to make it perminent
  }


  // -----------------------
  // getters & setters
  // -----------------------


  public Location getPlayerLocation() {
    return playerLocation;
  }

  public void setPlayerLocation(Location playerLocation) {
    this.playerLocation = playerLocation;
  }

  public Map<String, String> getLocToLinksHM() {
    return locToLinksHM;
  }

  public void setLocToLinksHM(Map<String, String> locToLinksHM) {
    this.locToLinksHM = locToLinksHM;
  }

  public Map<String, String> getLocToConnHM() {
    return locToConnHM;
  }

  public void setLocToConnHM(Map<String, String> locToConnHM) {
    this.locToConnHM = locToConnHM;
  }

  public Map<String, String> getLocToDesHM() {
    return locToDesHM;
  }

  public void setLocToDesHM(Map<String, String> locToDesHM) {
    this.locToDesHM = locToDesHM;
  }

  public String getMoveAttemptStatus() {
    return moveAttemptStatus;
  }

  public void setMoveAttemptStatus(String moveAttemptStatus) {
    this.moveAttemptStatus = moveAttemptStatus;
  }

  @Override
  public List<GameObject> getCurrentLocationGameObjects() {

    return this.playerLocation.getCurrentLocationGameObjects();
  }

  @Override
  public void addGameObjectToLocation( String locationId, GameObject gameObject) {

    Location someRoom = this.allRooms.get(locationId);
    someRoom.addGameObject(gameObject);

  }

  @Override
  public Location retrieveAnyLocation( String locationId) {

    return allRooms.get(locationId);
  }

  @Override
  public void replaceAnyLocation( String locationId, Location aLocationObject) {

    allRooms.put(locationId, aLocationObject);
  }


  @Override
  public void addGameObjectToCurrentLocation(GameObject gameObject) {
    this.playerLocation.addGameObject(gameObject);
  }

  @Override
  public void removeGameObjectFromCurrentLocation(GameObject gameObject) {

    this.playerLocation.removeGameObject(gameObject);

  }

  public Map<String, String> getLocToDirecDescHM() {
    return locToDirecDescHM;
  }

  public void setLocToDirecDescHM(HashMap<String, String> locToDirecDescHM) {
    this.locToDirecDescHM = locToDirecDescHM;
  }

  @Override
  public boolean isCurrentLocationExit()
  {
    return playerLocation.getIsExit();
  }
}
