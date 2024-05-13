package hwr.oop.chess.application;

import hwr.oop.chess.cli.InvalidUserInputException;

import java.util.Arrays;
import java.util.List;

public enum CellDirection {
  LEFT,
  RIGHT,
  TOP,
  TOP_LEFT,
  TOP_RIGHT,
  BOTTOM,
  BOTTOM_LEFT,
  BOTTOM_RIGHT;

  public static List<String> allCellDirections() {
    return Arrays.stream(CellDirection.values()).map(Enum::name).toList();
  }

  public static CellDirection fromStringToEnum(String direction) {
    direction = direction.toUpperCase();

    List<String> allTypes = allCellDirections();
    for (String cellDirection : allTypes) {
      if (cellDirection.equals(direction)) {
        return CellDirection.valueOf(cellDirection);
      }
    }
    throw new InvalidUserInputException("The cell direction '" + direction + "' is not valid.");
  }
}
