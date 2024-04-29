package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.FigureColor;

import java.util.logging.Logger;

public class ChessGame {
   private Logger logger;
    private Board board;

    public ChessGame() {
        logger = Logger.getLogger(getClass().getName());
        board = new Board(true);


        // Game goes on..



        if(board.isCheckmate(FigureColor.BLACK)){
            logger.info("Black lost");
        }
        if(board.isCheckmate(FigureColor.WHITE)){
            logger.info("White lost");
        }
    }
}
