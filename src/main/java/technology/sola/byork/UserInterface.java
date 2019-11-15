package technology.sola.byork;

import java.util.Scanner;

public class UserInterface {
  private static UserInterface instance = new UserInterface();
  private final Scanner scanner;

  private UserInterface() {
    scanner = new Scanner(System.in);
  }

  public static UserInterface getInstance() {
    return instance;
  }

  public String getInput() {
    return scanner.useDelimiter(System.lineSeparator()).next();
  }

  public void displayMessage(String message) {
    System.out.println(message); //NOSONAR
  }

  public void displayErrorMessage(String message) {
    System.err.println(message); //NOSONAR
  }
}

