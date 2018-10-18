package board;

import org.junit.Test;
import pieces.*;
import board.*;
import player.MoveTransition;
import player.ai.MiniMax;
import player.ai.MoveStrategy;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class BoardTest {
    
    @Test
    public void testAlgebreicNotation() {
        assertEquals(BoardUtils.getPositionAtCoordinate(0), "a8");
        assertEquals(BoardUtils.getPositionAtCoordinate(1), "b8");
        assertEquals(BoardUtils.getPositionAtCoordinate(2), "c8");
        assertEquals(BoardUtils.getPositionAtCoordinate(3), "d8");
        assertEquals(BoardUtils.getPositionAtCoordinate(4), "e8");
        assertEquals(BoardUtils.getPositionAtCoordinate(5), "f8");
        assertEquals(BoardUtils.getPositionAtCoordinate(6), "g8");
        assertEquals(BoardUtils.getPositionAtCoordinate(7), "h8");
    }
    
    @Test
    public void testFoolsMate(){
        
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"), 
                        BoardUtils.getCoordinateAtPosition("f3")));
        assertTrue(t1.getMoveStatus().isDone());
        
        final MoveTransition t2 = t1.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e7"), 
                        BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        
        final MoveTransition t3 = t2.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("g2"), 
                        BoardUtils.getCoordinateAtPosition("g4")));
        assertTrue(t3.getMoveStatus().isDone());
        
        final MoveStrategy strategy = new MiniMax(1);
        
        final Move aiMove = strategy.execute(t3.getTransitionBoard());
        
        final Move bestMove = Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d8"), 
                BoardUtils.getCoordinateAtPosition("h4"));
        
        assertEquals(aiMove, bestMove);
    }
}
