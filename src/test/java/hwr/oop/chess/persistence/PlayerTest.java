package hwr.oop.chess.persistence;

import hwr.oop.chess.application.figures.FigureColor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PlayerTest {
  @Test
  void createPlayer() {
    Player player = new Player("Theo", FigureColor.WHITE, 0);
    Assertions.assertThat(player.name()).isEqualTo("Theo");
    Assertions.assertThat(player.color()).isEqualTo(FigureColor.WHITE);
    Assertions.assertThat(player.elo()).isZero();
  }

  @Test
  void setColor() {
    Player player = new Player("Theo", FigureColor.WHITE, 0);
    player.setColor(FigureColor.BLACK);
    Assertions.assertThat(player.color()).isEqualTo(FigureColor.BLACK);
  }

  @Test
  void setName() {
    Player player = new Player("Theo", FigureColor.WHITE, 0);
    player.setName("Jonas");
    Assertions.assertThat(player.name()).isEqualTo("Jonas");
  }

  @Test
  void setElo() {
    Player player = new Player("Theo", FigureColor.WHITE, 0);
    player.setElo(10);
    Assertions.assertThat(player.elo()).isEqualTo(10);
  }
}
