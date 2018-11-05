package player;

import board.Board;
import board.Move;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import pieces.Alliance;
import pieces.King;
import pieces.Piece;


public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    
    Player(final Board board,
            final Collection<Move> legalMoves,
            final Collection<Move> opponentMoves){    
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        //this.legalMoves = Collections.unmodifiableList(merge(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
    }
    
    public King getPlayerKing(){
        return this.playerKing;
    }
    
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }
    
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move: moves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }
    

    private King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;                
            }
        }
        return null;
        //throw new RuntimeException("Should not reach here! not valid board!!");
    }
    
    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }
    
    public boolean canTakeKing(){
        for (final Move move: this.legalMoves){
            final MoveTransition transition = makeMove(move, false);
            if (transition.getTransitionBoard().currentPlayer().getOpponent().getPlayerKing() == null){
                return true;
            }
        }
        return false;
    }
    
    public boolean kingTaken(){
        if (null == getPlayerKing()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean opponentKingTaken(){
        if (null == this.board.currentPlayer().getOpponent().getPlayerKing()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean isCastled(){
        return false;
    }
    
    public MoveTransition makeMove(final Move move, boolean trueMove){ //trueMove indicates that the move can potentially effect the board)
        
        if(!this.legalMoves.contains(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        
        final Board transitionBoard = move.execute(trueMove);
            
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }
    
    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegal, Collection<Move> opponentsLegals);
}
