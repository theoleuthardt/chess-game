package hwr.oop.chess.persistence;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {
  @Test
  void playerHasInitialScore() {
    Player player = new Player(60, 1200, 0);
    assertThat(player.score()).isEqualTo(60.0);
    assertThat(player.elo()).isEqualTo(1200);
    assertThat(player.gameCount()).isEqualTo(0);
  }

  @Test
  void drawAddsHalfAPoint() {
    Player player = new Player(0, 1200, 0);
    player.adjustScoreOnGameEnd(0.5, 1200);
    assertThat(player.score()).isEqualTo(0.5);
    assertThat(player.elo()).isEqualTo(1200);
    assertThat(player.gameCount()).isEqualTo(1);
  }

  @Test
  void winAddsAFullPoint() {
    Player player = new Player(0, 1200, 0);
    player.adjustScoreOnGameEnd(1, 1200);
    assertThat(player.score()).isEqualTo(1.0);
    assertThat(player.elo()).isEqualTo(1220);
  }

  @Test
  void kOfNewPlayer() {
    Player player = new Player(0, 1200, 0);
    assertThat(player.kFactor()).isEqualTo(40);
  }

  @Test
  void eloOfNewPlayer() {
    Player player = new Player(0, 1200, 0);
    player.adjustScoreOnGameEnd(1, 2000);
    assertThat(player.elo()).isEqualTo(1239);
  }

  @Test
  void kOfMediumPlayer() {
    Player player = new Player(0, 1200, 30);
    assertThat(player.kFactor()).isEqualTo(20);
  }

  @Test
  void eloOfMediumPlayer() {
    Player player = new Player(0, 1200, 30);
    player.adjustScoreOnGameEnd(1, 2000);
    assertThat(player.elo()).isEqualTo(1219);
  }

  @Test
  void kOfOldPlayer() {
    Player player = new Player(0, 2400, 50);
    assertThat(player.kFactor()).isEqualTo(10);
  }

  @Test
  void eloOfOldPlayer() {
    Player player = new Player(0, 2400, 50);
    player.adjustScoreOnGameEnd(0.5, 1200);
    assertThat(player.elo()).isEqualTo(2395);
    assertThat(player.gameCount()).isEqualTo(51);
  }
}
