package technology.sola.byork;

public class Player {
  private boolean isPlaying = true;

  public void stopPlaying() {
    isPlaying = false;
  }

  boolean isPlaying() {
    return isPlaying;
  }

  // TODO implement
}
