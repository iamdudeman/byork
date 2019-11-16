package technology.sola.byork.map.impl;

import org.junit.jupiter.api.Test;
import technology.sola.byork.gameobjects.GameObject;
import technology.sola.byork.gameobjects.Scroll;
import technology.sola.byork.map.ByorkMap;
import technology.sola.byork.map.Direction;
import technology.sola.byork.map.Location;
import technology.sola.byork.map.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ByorkMapImplTest {
  String startingLocId = "0";
  Map<String, String> locToLinksHM;
  Map<String, String> locToConnHM;
  Map<String, String> locToDirDesc;
  Map<String, String> locToDesHM;


  /**
   * Review of handy enum stuff.
   */
  @Test
  void testEnumLook() {

    Direction compass;
    compass = Direction.NORTH;

    // if..else and switch treat it nice too.
    if (compass == Direction.WEST) {
      toConsole("yes, compass == NORTH");
    } else {
      toConsole("no, compass not == NORTH");
    }

    toConsole(".valueOf() =" + Direction.valueOf("NORTH")); // gives NORTH from string of "NORTH"
    toConsole(".ordinal() =" + Direction.WEST.ordinal());  // gives 3

    Direction[] foo = Direction.values(); // quick load an array.
    toConsole(foo[1].toString()); // gives  EAST compass object back
  }


  /**
   * Make standard test map, then look that all of its
   * attributes are displayed properly.
   */
  @Test
  final void testToString() {

    // SETUP
    ByorkMap zm = makeTestmap(0);

    // TEST
    String str = zm.toString();

    // OBSERVE
    toConsole(str);

    // ASSERT
    assertFalse(str.equals(""));
  }

  /**
   * Make a map.  put the player someplace. add an object to the location.
   */
  @Test
  final void testAddGameObjectToCurrentLocation() {

    // SETUP
    ByorkMapImpl zm = makeTestmap(0);
    GameObject gameObject = new Scroll("scroll: test addGameObjectToCurrentLocation");

    // TEST
    zm.addGameObjectToCurrentLocation(gameObject);
    zm.addGameObjectToCurrentLocation(new Scroll("scroll2: test addGameObjectToCurrentLocation"));
    zm.addGameObjectToCurrentLocation(new Scroll("scroll3: test addGameObjectToCurrentLocation"));

    // OBSERVE
    toConsole("Player is at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    this.toConsole(zm.toString());

    // ASSERT
    assertTrue(zm.getPlayerLocation().getCurrentLocationGameObjects().size() == 3);

  }

  /**
   * Make a map.  pick a room.  add an object to the location, see if it's there.
   */
  @Test
  final void testAddGameObjectToLocation() {

    // SETUP
    final String WHICH_ROOM = "1";
    ByorkMapImpl zm = makeTestmap(0);
    GameObject gameObject = new Scroll("scroll: test addGameObjectToCurrentLocation");

    // TEST
    zm.addGameObjectToLocation(WHICH_ROOM, gameObject);
    zm.addGameObjectToLocation(WHICH_ROOM, new Scroll("scroll2: test addGameObjectToCurrentLocation"));
    zm.addGameObjectToLocation(WHICH_ROOM, new Scroll("scroll3: test addGameObjectToCurrentLocation"));

    // OBSERVE
    toConsole("before: Player is at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    zm.move(Direction.EAST);
    toConsole("after: Description: " + zm.getPlayerLocation().getLocationDesc());
    this.toConsole(zm.getPlayerLocation().hasObjectsIn(zm.getPlayerLocation().getLocationID()));

    // ASSERT
    assertTrue(zm.getPlayerLocation().getCurrentLocationGameObjects().size() == 3);

  }

  /**
   * Make a map.  put the player someplace. add 3 objects to the location, then take 1st away.
   */
  @Test
  final void testRemoveGameObjectFromCurrentLocation() {

    // SETUP
    ByorkMapImpl zm = makeTestmap(0);
    GameObject gameObject = new Scroll("scroll: test addGameObjectToCurrentLocation");
    zm.addGameObjectToCurrentLocation(gameObject);
    zm.addGameObjectToCurrentLocation(new Scroll("scroll2: test addGameObjectToCurrentLocation"));
    zm.addGameObjectToCurrentLocation(new Scroll("scroll3: test addGameObjectToCurrentLocation"));
    this.toConsole("before: \r\n" + zm.toString());

    // TEST
    zm.removeGameObjectFromCurrentLocation(gameObject);

    // OBSERVE
    toConsole("Player is at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    this.toConsole("\r\nafter: \r\n" + zm.toString());

    // ASSERT
    assertTrue(zm.getPlayerLocation().getCurrentLocationGameObjects().size() == 2);

  }

  /**
   * put in three game objects, make sure you get them all as ordered.
   */
  @Test
  final void testGetCurrentLocationGameObjects() {

    // SETUP
    ByorkMapImpl zm = makeTestmap(0);
    GameObject gameObject = new Scroll("scroll: test addGameObjectToCurrentLocation");
    zm.addGameObjectToCurrentLocation(gameObject);
    zm.addGameObjectToCurrentLocation(new Scroll("scroll2: test addGameObjectToCurrentLocation"));
    zm.addGameObjectToCurrentLocation(new Scroll("scroll3: test addGameObjectToCurrentLocation"));

    // TEST
    assertTrue(zm.getCurrentLocationGameObjects().size() == 3);


  }

  /**
   * Try to make an impossible move.  Then try to make a possible move.
   */
  @Test
  final void testMove() {

    // SETUP
    ByorkMapImpl zm = makeTestmap(0);

    // TEST
    toConsole("Impossible: Player is at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getCurrentLocationDescription());
    zm.move(Direction.NORTH);
    toConsole(zm.getMoveAttemptStatus());

    // OBSERVE
    toConsole("Player is now at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc() + "\r\n");

    // ASSERT
    assertTrue(zm.getPlayerLocation().getLocationID() == 0);

    // TEST
    toConsole("Possible: Player is at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    zm.move(Direction.EAST);
    toConsole(zm.getMoveAttemptStatus());

    // OBSERVE
    toConsole("Player is now at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());


    // ASSERT
    assertTrue(zm.getPlayerLocation().getLocationID() == 1);

    // TEST
    toConsole("Impossible: Player is at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    zm.move(Direction.SOUTH);
    toConsole(zm.getMoveAttemptStatus());

    // OBSERVE
    toConsole("Player is now at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());

    // TEST
    toConsole("Possible: Player is at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    zm.move(Direction.WEST);
    toConsole(zm.getMoveAttemptStatus());

    // OBSERVE
    toConsole("Player is now at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());

    // ASSERT
    assertTrue(zm.getPlayerLocation().getLocationID() == 0);

  }

  /**
   * Try to move south from 3, then show that there is locked door south.
   * Try to move north from 4, and how locked door from there too.
   */
  @Test
  final void testGetDirectionsWithLockedDoorsForCurrentLocation() {

    // SETUP
    ByorkMapImpl zm = makeTestmap(3);

    // TEST
    toConsole("Before: Player starts at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    zm.move(Direction.SOUTH);
    toConsole(zm.getMoveAttemptStatus());

    List<Direction> foo = zm.getDirectionsWithLockedDoorsForCurrentLocation();

    // OBSERVE
    String str = foo.toString();
    toConsole(str);

    // ASSERT
    assertTrue(str.equals("[SOUTH]"), "ERROR: Should be [SOUTH]");

    // SETUP for north move now.
    zm = makeTestmap(4);

    // TEST
    toConsole("Before: Player starts at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    zm.move(Direction.NORTH);
    toConsole(zm.getMoveAttemptStatus());

    foo = zm.getDirectionsWithLockedDoorsForCurrentLocation();

    // OBSERVE
    str = foo.toString();
    toConsole(str);

    // ASSERT
    assertTrue(str.equals("[NORTH]"));

  }

  /**
   * Find a locked door, unlock it, move to next room.
   * <p>
   * 1. Try to move south from 3, then show that there is locked door south.
   * 2. unlock the door
   * 3. Try to move south again, and note success.
   * 4. Try to move north, and note success.
   */
  @Test
  final void test_setDoorStatusForCurrentLocation() {

    // SETUP
    ByorkMapImpl zm = makeTestmap(3);
    toConsole("Before: Player starts at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    zm.move(Direction.SOUTH);
    toConsole(zm.getMoveAttemptStatus());

    // TEST
    zm.setDoorStatusForCurrentLocation(Direction.SOUTH, false);

    // OBSERVE
    toConsole("After: Player starts at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getPlayerLocation().getLocationDesc());
    zm.move(Direction.SOUTH);
    toConsole(zm.getMoveAttemptStatus());

    // ASSERT
    assertTrue(zm.getMoveAttemptStatus().equals("3,SOUTH path movement completed and open"));

    // TEST
    zm.move(Direction.NORTH);
    toConsole(zm.getMoveAttemptStatus());

    // ASSERT
    assertTrue(zm.getMoveAttemptStatus().equals("4,NORTH path movement completed and open"));

  }

  /**
   * Put an object in a room.  move out of the room, then back in
   * to see if the object is still there.
   */
  @Test
  final void testMoveWithObject() {

    // SETUP
    ByorkMapImpl zm = makeTestmap(0);
    GameObject gameObject = new Scroll("Scroll O' Magic!");
    zm.addGameObjectToCurrentLocation(gameObject);

    toConsole(zm.getCurrentLocationDescription());

    /* move out of room */
    zm.move(Direction.EAST);
    toConsole(zm.getMoveAttemptStatus());
    toConsole(zm.getCurrentLocationDescription());

    /* move back in room */
    zm.move(Direction.WEST);
    toConsole(zm.getMoveAttemptStatus());
    toConsole(zm.getCurrentLocationDescription());

    /* get the scroll i put there */
    String str = zm.getPlayerLocation().getCurrentLocationGameObjects().get(0).getName().toString();


    assertTrue(str.equals("Scroll"));

  }

  /**
   * Make things dark at the destination. Then make a move there to see it's dark.
   *
   * @since 4.21.2015 This fails after Solum added a line to my kingdom at ByorkMap line 243.
   */
  @Test
  final void testMove_dark() {

    // SETUP
    ByorkMapImpl zm = makeTestmap(0);
    Location tempLoc = zm.retrieveAnyLocation("1");
    tempLoc.setIsDark(true);
    zm.replaceAnyLocation("1", tempLoc);

    // TEST
    toConsole("Start: Player is at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getCurrentLocationDescription());
    zm.move(Direction.EAST);


    // OBSERVE
    toConsole("End: Player is now at location: " + zm.getPlayerLocation().getLocationID());
    toConsole("Description: " + zm.getCurrentLocationDescription() + "\r\n");

    // ASSERT
    assertTrue(zm.getPlayerLocation().getLocationID() == 1);
  }

  /**
   * Put an object in a room.  move up out of the room, then back down
   * to see if the object is still there.
   */
  @Test
  final void testMoveWithObject_UpDn() {

    // SETUP
    ByorkMapImpl zm = makeTestmap(0);
    GameObject gameObject = new Scroll("Scroll O' Magic!");
    zm.addGameObjectToCurrentLocation(gameObject);

    toConsole(zm.getCurrentLocationDescription());

    /* move out of room */
    zm.move(Direction.UP);
    toConsole(zm.getMoveAttemptStatus());
    toConsole(zm.getCurrentLocationDescription());

    /* move back in room */
    zm.move(Direction.DOWN);
    toConsole(zm.getMoveAttemptStatus());
    toConsole(zm.getCurrentLocationDescription());

    /* get the scroll i put there */
    String str = zm.getPlayerLocation().getCurrentLocationGameObjects().get(0).getName().toString();


    assertTrue(str.equals("Scroll"));

  }

  /**
   * Move up and down and watch to see how directional descriptions change.
   */
  @Test
  final void testMove_with_directional_descriptions() {

    // SETUP
    ByorkMap zm = makeTestmap(0);
    toConsole(zm.getCurrentLocationDescription());

    // TEST
    /* move out of room */
    zm.move(Direction.UP);

    // OBSERVE 1
    toConsole(zm.getCurrentLocationDescription());

    /* move back in room */
    zm.move(Direction.DOWN);

    // OBSERVE 2
    toConsole(zm.getCurrentLocationDescription());

    /* move back in room */
    zm.move(Direction.SOUTH);

    // OBSERVE 3
    toConsole(zm.getCurrentLocationDescription());

    /* move back in room */
    zm.move(Direction.EAST);

    // OBSERVE 3
    toConsole(zm.getCurrentLocationDescription());

    // ASSERT


  }

  /**
   * Start at location 3, where a locked door is directly south in location 4.
   */
  @Test
  final void testMove_with_directional_descriptions_2() {

    // SETUP
    ByorkMap zm = makeTestmap(3);
    toConsole(zm.getCurrentLocationDescription());

    // TEST
    /* move toward locked door. */
    zm.move(Direction.SOUTH);

    // OBSERVE 1
    toConsole(zm.getCurrentLocationDescription());

  }


  // --------------------------
  // Utility
  // --------------------------


  /**
   * Just like above, BUT now with up and down added.
   *
   * @param loc where the player starts.
   * @return a ByorkMap object which is traversable.
   */
  private ByorkMapImpl makeTestmap(int loc) {

    this.startingLocId = MapUtils.intToStr(loc);

    locToLinksHM = new HashMap<>();
    locToLinksHM.put("0", "U,1,2,U,5,U");
    locToLinksHM.put("1", "U,U,U,0,U,U");
    locToLinksHM.put("2", "0,3,U,U,U,U");
    locToLinksHM.put("3", "U,U,4,2,U,U");
    locToLinksHM.put("4", "3,U,U,U,U,U");
    locToLinksHM.put("5", "3,U,U,U,U,0");

    locToConnHM = new HashMap<>();
    locToConnHM.put("0", "U,O,O,U,O,U");
    locToConnHM.put("1", "U,U,U,O,U,U");
    locToConnHM.put("2", "O,O,U,U,U,U");
    locToConnHM.put("3", "U,U,C,O,U,U");
    locToConnHM.put("4", "C,U,U,U,U,U");
    locToConnHM.put("5", "U,U,U,U,U,O");

    locToDirDesc = new HashMap<>();
    locToDirDesc.put("0", "U,U,a screen door,U,an attic entry,U");
    locToDirDesc.put("1", "U,U,U,U,U,U");
    locToDirDesc.put("2", "U,U,U,U,U,U");
    locToDirDesc.put("3", "U,U,an ugly wooden door,U,U,U");
    locToDirDesc.put("4", "U,U,U,U,U,U");
    locToDirDesc.put("5", "U,U,U,U,U,a stairway");

    locToDesHM = new HashMap<>();
    locToDesHM.put("0", "0 is in a house.");
    locToDesHM.put("1", "1 is in front yard.");
    locToDesHM.put("2", "2 is in back yard.");
    locToDesHM.put("3", "3 is in the shed.");
    locToDesHM.put("4", "4 is in a cellar.");
    locToDesHM.put("5", "5 is in the attic.");

    return new ByorkMapImpl(this.startingLocId, locToLinksHM, locToConnHM, locToDirDesc, locToDesHM);
  }


  /**
   * Quick way to use console and yet turn off for all tests at once when test
   * building is complete.  Use '//' in front of System... below.
   *
   * @param str a string to be displayed.
   */
  void toConsole(String str) {

    //System.out.println( str );
  } // end method
}
