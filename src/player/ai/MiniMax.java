package player.ai;

import board.Board;
import board.Move;
import player.MoveTransition;
import java.util.Random;

public class MiniMax implements MoveStrategy{

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    
    public MiniMax(final int searchDepth){
        this.searchDepth = searchDepth;
        this.boardEvaluator = new StandardBoardEvaluator();
    }
    
    @Override
    public String toString(){
        return "MiniMax";
    }
    
    @Override
    public Move execute(Board board) {
        
        final long startTime = System.currentTimeMillis();
        
        Move bestMove = null;
        
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        int randomInt;
        
        //System.out.println(board.currentPlayer() + " Enemy AI thinking");
        
        int numMoves = board.currentPlayer().getLegalMoves().size();
        //System.out.println("There are "+numMoves + " possible moves");
        
        for(final Move move : board.currentPlayer().getLegalMoves()){
            
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move, false);
            if(moveTransition.getMoveStatus().isDone()){
                
                currentValue = board.currentPlayer().getAlliance().isWhite() ?  
                        min(moveTransition.getTransitionBoard(), this.searchDepth -1, highestSeenValue) : //white is maximizing player
                        max(moveTransition.getTransitionBoard(), this.searchDepth -1, lowestSeenValue); //black is the maximizing player
                
                if(board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue){ //white wants highest seen score
                        //highestSeenValue = currentValue;
                        if(currentValue == highestSeenValue){
                            randomInt = new Random().nextInt(10);
                            if(randomInt > 5){
                                highestSeenValue = currentValue;
                                bestMove = move;
                            }
                            else
                                ;//do not take currentValue
                        }else{
                            highestSeenValue = currentValue;
                            bestMove = move;
                        }
                        //bestMove = move;
                }else if(board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue){ //black wants lowest seen score
                        //lowestSeenValue = currentValue;
                        if(currentValue == lowestSeenValue){
                            randomInt = new Random().nextInt(10);
                            if(randomInt > 5){
                                lowestSeenValue = currentValue;
                                bestMove = move;
                            }
                            else
                                ;//do not take currentValue
                        }else{
                            lowestSeenValue = currentValue;
                            bestMove = move;
                        }
                            
                        //bestMove = move;
                }
            }
        }
        
        final long executionTime = System.currentTimeMillis() - startTime;
        //System.out.println("Total time spent calculating: "+ executionTime + " seconds");
        
        return bestMove;
    }
    
    public int min(final Board board, final int depth, int prevScore){
        
        int newScore = this.boardEvaluator.evaluate(board, depth); //calculate the board's score
        
        if(depth == 0 || isEndGameScenario(board)){ //if finished evaluating or game over
            //return this.boardEvaluator.evaluate(board, depth);
            return newScore; //stops looking deeper in current branch
        }else if(newScore <= prevScore){ //if min's newScore is lower than max's score
            return newScore; //stops looking deeper in current branch
        }

        
        int lowestSeenValue = Integer.MAX_VALUE; //starts with highest possible value
        for(final Move move : board.currentPlayer().getLegalMoves()){ //goes through each possible move at current layer
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move, false); //makes each move possible
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getTransitionBoard(), depth -1, newScore);
                if(currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue; //record lowest value seen in all possible moves
                }
            }
        }
        return lowestSeenValue; //returns lowest value
    }
    
    private static boolean isEndGameScenario(final Board board){
        return (board.currentPlayer().kingTaken() || board.currentPlayer().getOpponent().kingTaken());
    }
    
    public int max(final Board board, final int depth, int prevScore){
        
        int newScore = this.boardEvaluator.evaluate(board, depth); //calculate the board's score
        
        if(depth == 0 || isEndGameScenario(board)){ //if finished evaluating or game over
            //return this.boardEvaluator.evaluate(board, depth);
            return newScore; //stops looking deeper in current branch
        }else if(newScore >= prevScore){ //if max's newScore is higher than min's score
            return newScore; //stops looking deeper in current branch
        }
        
        int highestSeenValue = Integer.MIN_VALUE; //starts with smallest possible value
        for(final Move move : board.currentPlayer().getLegalMoves()){ //goes through each possible move at current layer
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move, false); //makes each move possible
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = min(moveTransition.getTransitionBoard(), depth -1, newScore);
                if(currentValue >= highestSeenValue){
                    highestSeenValue = currentValue; //record highest value seen in all possible moves
                }
            }
        }
        return highestSeenValue; //returns highest value
    }
}
