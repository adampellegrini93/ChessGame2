package pieces;

import board.Board;
import board.BoardUtils;
import board.Move;
import board.Move.MajorMove;
import board.Move.PawnAttackMove;
import board.Move.PawnEnPassantAttackMove;
import board.Move.PawnJump;
import board.Move.PawnMove;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Pawn extends Piece{
    
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9, 16, 18, -2};

    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }
    
    public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            
            int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset); 
            
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            //for moving backwards
            if((currentCandidateOffset == -8 && (!board.getTile(candidateDestinationCoordinate).isTileOccupied())) &&
                    ((this.pieceAlliance.isWhite() && !BoardUtils.FIRST_RANK[this.piecePosition]) || 
                    (this.pieceAlliance.isBlack() && !BoardUtils.EIGHTH_RANK[this.piecePosition]))){
                legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
            }
            //for moving forwards
            else if((currentCandidateOffset == 8 && (!board.getTile(candidateDestinationCoordinate).isTileOccupied())) &&
                    ((this.pieceAlliance.isWhite() && !BoardUtils.EIGHTH_RANK[this.piecePosition]) || 
                    (this.pieceAlliance.isBlack() && !BoardUtils.FIRST_RANK[this.piecePosition]))){
                legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
            }
            //for moving to left and diagnally backwards to the left
            else if((currentCandidateOffset == 1 || currentCandidateOffset == -7) && 
                    (!board.getTile(candidateDestinationCoordinate).isTileOccupied()) &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
                    (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
            }
            //for moving to the right and diagnally backwards to the right
            else if((currentCandidateOffset == -1 || currentCandidateOffset == -9) && 
                    (!board.getTile(candidateDestinationCoordinate).isTileOccupied()) &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
            }
            //for pawn being able to move 2 spaces on first move  
            else if(currentCandidateOffset == 16 && this.isFirstMove() &&  
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) || 
                    (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))){
                final int behindCandidateDesitinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(behindCandidateDesitinationCoordinate).isTileOccupied() && 
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                } 
            //for pawn moving diagnally to the front right 
            }else if((currentCandidateOffset == 7) &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){ 
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate));
                    }
                }else if((board.getEnPassantPawn() != null) && board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                }
                else{
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
                //for pawn moving diagnally to the front left
            }else if((currentCandidateOffset == 9) &&
                !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
                    (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }else if(board.getEnPassantPawn() != null && 
                        (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))))
                {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                } 
                else{
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            }
        }     
        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
    
}
