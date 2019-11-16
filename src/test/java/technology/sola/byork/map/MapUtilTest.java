package technology.sola.byork.map;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MapUtilTest {
  @Test
  void testCsvToArray() {

    String str = "4,3,2,1";
    String[] strArr = MapUtils.csvToArray(str);
    assertTrue(strArr[0].equals("4"));
    this.toConsole(Arrays.toString(strArr));
  }

  @Test
  void testStrToInt() {
    String str = " 33\t"; // space, then 33, then tab
    int x = MapUtils.strToInt(str);
    assertTrue(x == 33);
  }

  @Test
  void testStrsToInts() {

    String[] strArr = {"4", "3", "2", "1"};
    int[] intArr = MapUtils.strsToInts(strArr);
    assertTrue(intArr[0] == 4);
    this.toConsole(Arrays.toString(intArr));

  }

  @Test
  void testRtnKey() {

    String str = "1=U,2,U,4";
    int key = MapUtils.rtnKey(str);
    assertTrue(key == 1);
  }

  @Test
  void testRtnVal() {

    String str = "1=U,2,U,4";
    String val = MapUtils.rtnVal(str);
    assertTrue(val.equals("U,2,U,4"));

  }


  // --------------------------
  // Utility
  // --------------------------

  /**
   * Quick way to use console and yet turn off for all tests at once when test
   * building is complete.  Use '//' in front of System... below.
   *
   * @param str a string to be displayed.
   */
  void toConsole(String str) {

    //System.out.println( str );
  }
}
