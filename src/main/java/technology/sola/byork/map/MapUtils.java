package technology.sola.byork.map;

import com.google.gson.*;
import technology.sola.byork.gameobjects.*;
import technology.sola.byork.map.impl.ByorkMapImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 *
 * A collection of useful utilities in dealing with map data.
 * @author Brint
 * @version created 4/2/15
 *
 */
public class MapUtils {
  /**
   * Given a CSV string (e.g. "1,2,3,4"), separate each element of that string
   * and and place into a string array.
   * @return csvString a string array holding the elements earlier separated by commas.
   */
  public static String[] csvToArray(String csvString) {

    String[] values = csvString.split(",");

    return values;
  }

  /**
   * Given a string, even with white space, return the integer value of that
   * string.
   *
   * @param str
   *            a string, possibly with white space.
   * @return an integer representation of the string.
   */
  public static int strToInt(String str) {

    int i = Integer.parseInt(str.trim());

    return i;
  }

  /**
   * Convert an integer to a string
   *
   * @param newLocId an integer
   * @return a string
   */
  public static String intToStr(int newLocId) {

    return Integer.toString(newLocId);
  }

  /**
   * Given a string array of values, convert it to an integer array of values
   *
   * @param strArr
   *            a string array of values
   * @return an integer array of values.
   */
  public static int[] strsToInts(String[] strArr) {

    int size = strArr.length;

    int[] intArr = new int[size];

    for (int i = 0; i < size; i++) {
      intArr[ i ] = MapUtils.strToInt(strArr[ i ]);
    }

    return intArr;
  }

  /**
   * Convert a string array to a csv string.<p>
   * e.g. "[U][U][C][O]" to "U,U,C,O"
   *
   * @param strArr
   *            a string array of values
   *
   * @return an csv string with the values provided.
   */
  public static String arrayToCSV(String[] strArr) {

    String str = "";

    for (int i = 0; i < strArr.length; i++) {
      str += strArr[ i ]+",";
    }

    return str;
  }


  /**
   * Given a key=value pair string, return just the key as an integer.
   *
   * @param hashElement
   *            a key=value pair string (e.g. "1=U,2,U,4")
   * @return the key of a key=value pair string as an integer.
   */
  public static int rtnKey(String hashElement) {

    String[] key = hashElement.split("=");
    int intKey = Integer.parseInt(key[0].trim());

    return intKey ;
  }

  /**
   * Given a key=value pair string, return just the value as a string.
   *
   * @param hashElement
   *            a key=value pair string (e.g. "1=U,2,U,4")
   * @return the value of a key=value pair string as a string.
   */
  public static String rtnVal( String hashElement) {

    String[] values = hashElement.split("=");
    String strValue = (values[1].trim());

    return strValue ;
  }

  public static ByorkMap loadMap( String filename )
  {
    // If filename is null return null right away
    if( filename == null )
    {
      return null;
    }

    Map<String, List<GameObject>> locationGameObjects = new HashMap<>();
    List<Boolean> darkLocations = new LinkedList<>();
    List<Boolean> exitLocations = new LinkedList<>();

    JsonParser jsonParser = new JsonParser();
    JsonObject root = null;

    HashMap<String, String> locToLinksHM = new HashMap<>();
    HashMap<String, String> locToConnHM = new HashMap<>();
    HashMap<String, String> locToDirecDescHM = new HashMap<>();
    HashMap<String, String> locToDesHM = new HashMap<>();

    try
    {
      root = jsonParser.parse( new FileReader( new File( filename ) ) ).getAsJsonObject();
      int index = 0;
      while(root.has( "" + index ))
      {
        // General information
        String locationId = "" + index;
        JsonObject locationObject = root.get( locationId ).getAsJsonObject();
        String locationDescription = locationObject.get( "description" ).getAsString();
        boolean isDark = locationObject.get( "dark" ).getAsBoolean();
        boolean isExit = locationObject.get( "exit" ).getAsBoolean();

        // Keep track of whether or not this location should be set to dark and exit later
        darkLocations.add( isDark );
        exitLocations.add( isExit );

        // Add a list for game objects for this location
        locationGameObjects.put( locationId, new ArrayList<GameObject>() );

        // Game object stuff
        Iterator<JsonElement> iterObjects = locationObject.get( "objects" ).getAsJsonArray().iterator();
        while(iterObjects.hasNext())
        {
          JsonObject currentObject = iterObjects.next().getAsJsonObject();
          String objectName = currentObject.get( "name" ).getAsString();
          GameObject gameObject = null;
          switch(objectName.toLowerCase())
          {
            case "key":
              gameObject = new Key( null );
              break;
            case "flashlight":
              int batteryLife  = currentObject.get( "battery" ).getAsInt();
              gameObject = new Flashlight( batteryLife );
              break;
            case "scroll":
              String helpfulMessage = currentObject.get( "wisdom" ).getAsString();
              gameObject = new Scroll( helpfulMessage );
              break;
            case "treasure":
              int score = currentObject.get( "score" ).getAsInt();
              gameObject = new Treasure( score );
              break;
          }

          locationGameObjects.get( locationId ).add( gameObject );
        }

        // Declare directions information, set them to undefined as default
        String northLink = "U";
        String eastLink = "U";
        String southLink = "U";
        String westLink = "U";
        String upLink = "U";
        String downLink = "U";

        String northConn = "U";
        String eastConn = "U";
        String southConn = "U";
        String westConn = "U";
        String upConn = "U";
        String downConn = "U";

        String northDesc = "U";
        String eastDesc = "U";
        String southDesc = "U";
        String westDesc = "U";
        String upDesc = "U";
        String downDesc = "U";

        // Connections stuff
        Iterator<JsonElement> iterConnections = locationObject.get( "connections" ).getAsJsonArray().iterator();
        while(iterConnections.hasNext())
        {
          JsonObject currentConnection = iterConnections.next().getAsJsonObject();
          String connectionId = currentConnection.get( "id" ).getAsString();
          String connectionDescription = currentConnection.get( "description" ).getAsString();
          String connectionDirection = currentConnection.get( "direction" ).getAsString();
          boolean isLocked = currentConnection.get( "locked" ).getAsBoolean();

          switch(connectionDirection.toLowerCase())
          {
            case "north":
              northLink = connectionId;
              northConn = isLocked ? "C" : "O";
              northDesc = connectionDescription;
              break;
            case "east":
              eastLink = connectionId;
              eastConn = isLocked ? "C" : "O";
              eastDesc = connectionDescription;
              break;
            case "south":
              southLink = connectionId;
              southConn = isLocked ? "C" : "O";
              southDesc = connectionDescription;
              break;
            case "west":
              westLink = connectionId;
              westConn = isLocked ? "C" : "O";
              westDesc = connectionDescription;
              break;
            case "up":
              upLink = connectionId;
              upConn = isLocked ? "C" : "O";
              upDesc = connectionDescription;
              break;
            case "down":
              downLink = connectionId;
              downConn = isLocked ? "C" : "O";
              downDesc = connectionDescription;
              break;
          }
        }

        // Put stuff in the hash maps
        // N,E,S,W,U,D
        locToDesHM.put( locationId, locationDescription );
        locToLinksHM.put( locationId, String.format( "%s,%s,%s,%s,%s,%s", northLink, eastLink, southLink, westLink, upLink, downLink ) );
        locToDirecDescHM.put( locationId, String.format( "%s,%s,%s,%s,%s,%s", northDesc, eastDesc, southDesc, westDesc, upDesc, downDesc ) );
        locToConnHM.put( locationId, String.format( "%s,%s,%s,%s,%s,%s", northConn, eastConn, southConn, westConn, upConn, downConn ) );

        // Increment teh index (intentional misspelling)
        index++;
      }
    }
    catch( JsonIOException | JsonSyntaxException | FileNotFoundException e )
    {
      e.printStackTrace();
    }

    // Create the zork map
    ByorkMap byorkMap = new ByorkMapImpl("0", locToLinksHM, locToConnHM, locToDirecDescHM, locToDesHM);

    // Add the game objects to their proper locations
    Iterator<String> iterLocation = locationGameObjects.keySet().iterator();
    while(iterLocation.hasNext())
    {
      String key = iterLocation.next();
      Iterator<GameObject> iterGameObject = locationGameObjects.get( key ).iterator();
      while(iterGameObject.hasNext())
      {
        GameObject gameObject = iterGameObject.next();
        // Work around to get Key objects to work
        if(gameObject.getName().toLowerCase().equals( "Key" ))
        {
          Key temp = (Key)gameObject;
          temp.setZorkMap( byorkMap );
        }
        byorkMap.addGameObjectToLocation( key, gameObject );
      }
    }

    // Make locations dark where appropriate
    int darkIndex = 0;
    Iterator<Boolean> iterDarkLocations = darkLocations.iterator();
    while(iterDarkLocations.hasNext())
    {
      byorkMap.retrieveAnyLocation( "" + darkIndex ).setIsDark( iterDarkLocations.next() );
      darkIndex++;
    }

    // Make locations exits where appropriate
    int exitIndex = 0;
    Iterator<Boolean> iterExitLocations = exitLocations.iterator();
    while(iterExitLocations.hasNext())
    {
      byorkMap.retrieveAnyLocation( "" + exitIndex ).setIsExit( iterExitLocations.next() );
      exitIndex++;
    }

    return byorkMap;
  }
}
