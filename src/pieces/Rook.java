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

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -8, -1, 1, 8 };
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};
    
    public Rook(final Alliance pieceAlliance, 
            final int piecePosition) {
        super(PieceType.ROOK, piecePosition, pieceAlliance, true);
    }
    
    public Rook(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
        super(PieceType.ROOK, piecePosition, pieceAlliance, isFirstMove);
    }

 @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        //for regular piece movement ability
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
        //for fuzzy logic move ability
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            //for moving diagnally backwards to the right/and diagnally forward to the right
            else if((currentCandidateOffset == -9 || currentCandidateOffset == 7) && 
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                if (!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) { //if on enemy piece
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
            //for moving diagnally backwards to the left/ and diagnally forward to the left
            else if((currentCandidateOffset == -7 || currentCandidateOffset == 9) && 
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
                    (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                if (!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) { //if on enemy piece
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
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
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }
    
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1);
    }
}
