package pieces;

import board.Board;
import board.BoardUtils;
import board.Move;
import board.Move.MajorAttackMove;
import board.Move.MajorMove;
import board.Tile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Rook extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };
    
    public Rook(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.ROOK, piecePosition, pieceAlliance, true);
    }
    
    public Rook(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
        super(PieceType.ROOK, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES) {//loops through legal moves        
            int candidateDestinationCoordinate = this.piecePosition;           
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){       
                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                    isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                } 
                candidateDestinationCoordinate += candidateCoordinateOffset; //applys offset to current position    
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    if ((candidateDestinationCoordinate != (this.piecePosition + (candidateCoordinateOffset * 5))) &&
                        (candidateDestinationCoordinate != (this.piecePosition + (candidateCoordinateOffset * 6))) &&
                        (candidateDestinationCoordinate != (this.piecePosition + (candidateCoordinateOffset * 7)))){ //do not consider movements greater than 4 spaces   
                        final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                        if (!candidateDestinationTile.isTileOccupied()) { //if not occupied by another piece currently
                            legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                        } else { //if desination is currently occupied
                            final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                            final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                            if (this.pieceAlliance != pieceAlliance) { //if on enemy piece
                                legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                            }
                            break;
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public Rook movePiece(final Move move) {
        return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }
    
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
    }
    
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1|| candidateOffset == 9);
    }
}
