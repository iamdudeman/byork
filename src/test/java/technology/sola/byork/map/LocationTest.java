package technology.sola.byork.map;

import org.junit.jupiter.api.Test;
import technology.sola.byork.gameobjects.GameObject;
import technology.sola.byork.gameobjects.Scroll;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

  /**
   * Show everything you know
   */
  @Test
  final void testToString() {

    // SETUP
    Location location = new Location();
    int locationId = 1;
    boolean isDark = false;
    String[] locationLinks = {"3", "0", "U", "2", "U", "U"};
    String[] locationConnTypes = {"U", "O", "O", "U", "U", "U"};
    String[] locationDirecDescs = {"U", "a screen door", "U", "U", "U", "U"};
    String locationDesc = "1 = this is a house.";

    String expected =
      "locationID.......... 1\r\n" +
        "isDark.............  false\r\n" +
        "locationLinks....... [3, 0, U, 2, U, U]\r\n" +
        "locationConnTypes... [U, O, O, U, U, U]\r\n" +
        "locationDirecDescs.. [U, a screen door, U, U, U, U]\r\n" +
        "locationDesc:....... 1 = this is a house.\r\n" +
        "These objects are in the room:\r\n" +
        "  none\r\n";

    location.setLocationID(locationId);
    location.setIsDark(isDark);
    location.setLocationLinks(locationLinks);
    location.setLocationConnTypes(locationConnTypes);
    location.setLocationDirecDescs(locationDirecDescs);
    location.setLocationDesc(locationDesc);
    location.setCurrentLocationGameObjects(new ArrayList<>());

    // TEST
    String str = (location.toString());

    // OBSERVE
    toConsole(str);

    // ASSERT
    assertEquals(expected, str, "ERROR: unexpected output:\r\n" + str);
  } // end method

  /**
   * Given a direction, return the polar opposite.
   */
  @Test
  final void testCalcOppositeDirection() {
    Location location = new Location();
    Direction org = Direction.WEST;
    Direction opp = location.calcOppositeDirection(org);

    this.toConsole(org + " --> " + opp);

    assertSame(opp, Direction.EAST);
  }

  /**
   * Put in the 4-tuple, make sure the right stuff is extracted
   * into the right array elements.
   */
  @Test
  final void testExtractLocationLinks() {

    // SETUP
    Location location = new Location();
    String links = "3,0,U,2";

    // TEST
    String[] locationLinks = location.extractLocationLinks(links);

    // OBSERVE
    toConsole(Arrays.toString(locationLinks));

    // ASSERT
    assertEquals(4, locationLinks.length);
    assertEquals("3", locationLinks[0]);
    assertEquals("U", locationLinks[2]);
    assertEquals("2", locationLinks[3]);
  }

  /**
   * Put in the 4-tuple of connections, make sure the right stuff is extracted
   * into the right array elements.
   */
  @Test
  final void testExtractConnTypes() {

    // SETUP
    Location location = new Location();
    String conns = "U,O,C,U";

    // TEST
    String[] connectionTypes = location.extractConnTypes(conns);

    // OBSERVE
    toConsole(Arrays.toString(connectionTypes));

    // ASSERT
    assertEquals(4, connectionTypes.length);
    assertEquals("U", connectionTypes[0]);
    assertEquals("C", connectionTypes[2]);
    assertEquals("U", connectionTypes[3]);
  }

  @Test
  final void testExtractDescription() {

    // SETUP
    Location location = new Location();
    String testDesc = ("This is a white house.");

    // TEST
    String desc = location.extractDescription(testDesc);

    toConsole(desc);

    // ASSERT
    assertEquals(testDesc, desc);
  }

  /**
   * put a couple of directions in, make sure the right location comes
   * comes out, if there is one; if not, make sure it's seen as undefined.
   */
  @Test
  final void testHasDirectionAt() {

    // SETUP
    Location location = new Location();
    String[] locationLinks = {"3", "0", "U", "2"};
    location.setLocationLinks(locationLinks);
    int locId = -999;

    // TEST
    locId = location.hasLocationAt(Direction.NORTH);
    // ASSERT
    assertEquals(3, locId);

    // TEST
    locId = location.hasLocationAt(Direction.SOUTH);
    // ASSERT
    assertEquals(-1, locId); // undefined.
    // OBSERVE
    toConsole(Arrays.toString(locationLinks) + " --> " + locId + "");


  }

  /**
   * put a couple of directions in, make sure the right conn type comes
   * comes out.
   */
  @Test
  final void testHasConnectionTypeAt() {

    // SETUP
    Location location = new Location();
    String[] connectionTypes = {"U", "O", "C", "U"};
    location.setLocationConnTypes(connectionTypes);
    ;
    String connType = "";

    // TEST
    connType = location.hasConnectionTypeAt(Direction.NORTH);
    // ASSERT
    assertTrue(connType.equals("U"));

    // TEST
    connType = location.hasConnectionTypeAt(Direction.EAST);
    // ASSERT
    assertTrue(connType.equals("O"));

    // TEST
    connType = location.hasConnectionTypeAt(Direction.SOUTH);
    // ASSERT
    assertTrue(connType.equals("C"));

  }

  @Test
  public final void testLocation() {

    // SET UP
    Location location = new Location("1", "U,2,3,U", "U,O,C,U", "U,a screen door,U,U,U,U", "You are in a house.");

    // OBSERVE
    toConsole(location.toString());

    // ASSERT
    assertFalse(location.getLocationDesc().equals(""));
    assertFalse(location.getLocationDesc().equals(null));


  }

  /**
   * Put you in location 3, add 2 scrolls, take one away.
   */
  @Test
  final void test_addGameObject2() {

    // SET UP
    Location location = new Location("3", "U,U,4,2", "U,U,C,O", "U,a screen door,U,U,U,U", "You are in a shed.");
    GameObject obj;
    GameObject temp;
    // TEST
    obj = new Scroll("#1 Testing, so shut up.");
    location.addGameObject(obj);
    obj = new Scroll("#2 Testing, so shut up.");
    temp = obj;
    location.addGameObject(obj);
    obj = new Scroll("#3 Testing, so shut up.");
    location.addGameObject(obj);

    // OBSERVE
    toConsole("All three:\r\n" + location.toString());

    // ASSERT
    assertTrue(location.getCurrentLocationGameObjects().size() == 3);

    // TEST
    location.removeGameObject(temp);

    // ASSERT
    assertTrue(location.getCurrentLocationGameObjects().size() == 2);

    // OBSERVE
    toConsole("Just two:\r\n" + location.toString());

    // TEST
    boolean result = location.removeGameObject(temp); // try to remove it twice.

    // OBSERVE
    toConsole("No change:\r\n" + location.toString());

    // ASSERT
    assertFalse(result);
    assertTrue(location.countGameObjects() == 2);


  }

  /**
   * Quick way to use console and yet turn off for all tests at once when test
   * building is complete.  Use '//' in front of System... below.
   *
   * @param str a string to be displayed.
   */
  private void toConsole(String str) {

    // System.out.println( str );
  } // end method
}
