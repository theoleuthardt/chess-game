package hwr.oop.chess.persistence;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {
  @Test
  void playerHasInitialScore() {
    Player player = new Player("120");
    assertThat(player.doubleOfScore()).isEqualTo(120);
    assertThat(player.score()).isEqualTo(60.0);
  }

  @Test
  void drawAddsHalfAPoint() {
    Player player = new Player("0");
    player.halfPointOnDraw();
    assertThat(player.score()).isEqualTo(0.5);
  }

  @Test
  void winAddsAFullPoint() {
    Player player = new Player("0");
    player.fullPointOnWin();
    assertThat(player.score()).isEqualTo(1.0);
  }
}
