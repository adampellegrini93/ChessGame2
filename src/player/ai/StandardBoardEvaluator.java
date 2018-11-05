package player.ai;

import board.Board;
import pieces.Piece;
import player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int TAKE_KING_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;
    
    public StandardBoardEvaluator() {
        
    }

    @Override
    public int evaluate(Board board, int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) -
                scorePlayer(board, board.blackPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) { //returns players current score
        return pieceValue(player) + mobility(player) + //takes into account mobility of player and if their in checkmate or castled
                takeKing(player, depth) + castled(player); 
    }
    
    private static int castled(Player player){
        return player.isCastled() ? CASTLE_BONUS : 0;
    }
    
    private static int takeKing(final Player player, int depth){
        return player.canTakeKing() ? TAKE_KING_BONUS * depthBonus(depth): 0;
    }
    
    private static int depthBonus(int depth) {
        return depth == 0? 1 : DEPTH_BONUS * depth;
    }
    
    private static int mobility(final Player player){
        return player.getLegalMoves().size();
    }
    
    private static int pieceValue(final Player player){
        int pieceValueScore = 0;
        for(final Piece piece : player.getActivePieces()){
            pieceValueScore += piece.getPieceValue();
                    
        }
        return pieceValueScore;
    }
    
    
    public static boolean isEndGame(final Board board){
        return (board.currentPlayer().kingTaken() || board.currentPlayer().getOpponent().kingTaken());
    }
    
}
