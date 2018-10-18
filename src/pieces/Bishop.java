package pieces;

import board.Board;
import board.BoardUtils;
import board.Move;
import board.Move.AttackMove;
import board.Move.MajorAttackMove;
import board.Move.MajorMove;
import board.Tile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Bishop extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9};
    private final static int[] CANDIDATE_MOVE_COORDINATES = { -8, -1, 1, 8};
    public Bishop(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance,true);
    }
    
    public Bishop(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {   
        final List<Move> legalMoves = new ArrayList<>();
        //for regualr piece movement ability
        for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES) {//loops through legal moves        
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){       
                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                    isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                } 
                candidateDestinationCoordinate += candidateCoordinateOffset; //applys offset to current position    
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    MajorMove temp = new MajorMove(board, this,candidateDestinationCoordinate);
                    if (!legalMoves.contains(temp)) {
                        final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                        if (!candidateDestinationTile.isTileOccupied()) { //if not occupied by another piece currently
                            legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
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
        //for fuzzy logic movement ability
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset); 
            
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            //for moving backwards
            if((currentCandidateOffset == -8 && (!board.getTile(candidateDestinationCoordinate).isTileOccupied())) &&
                    ((this.pieceAlliance.isWhite() && !BoardUtils.FIRST_RANK[this.piecePosition]) || 
                    (this.pieceAlliance.isBlack() && !BoardUtils.EIGHTH_RANK[this.piecePosition]))){
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            }
            //for moving forward
            else if((currentCandidateOffset == 8 && (!board.getTile(candidateDestinationCoordinate).isTileOccupied())) &&
                    ((this.pieceAlliance.isWhite() && !BoardUtils.EIGHTH_RANK[this.piecePosition]) || 
                    (this.pieceAlliance.isBlack() && !BoardUtils.FIRST_RANK[this.piecePosition]))){
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            }
            //for moving to left
            else if((currentCandidateOffset == 1) && 
                    (!board.getTile(candidateDestinationCoordinate).isTileOccupied()) &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
                    (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            }
            //for moving to the right
            else if((currentCandidateOffset == -1) && 
                    (!board.getTile(candidateDestinationCoordinate).isTileOccupied()) &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            }
        }
        return Collections.unmodifiableList(legalMoves);
        
    }
    
    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }
    
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
    }
    
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
    }      
}