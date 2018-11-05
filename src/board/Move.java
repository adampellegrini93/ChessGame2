package board;

import java.lang.Math;
import board.Board.Builder;
import pieces.Pawn;
import pieces.Piece;
import pieces.Rook;

public abstract class Move {
    
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;
    
    public static final Move NULL_MOVE = new NullMove();
    
    private Move(final Board board, 
            final Piece movedPiece, 
            final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    
    private Move(final Board board,
            final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }
    
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1; 
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }
    
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }
    
    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }
    
    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }
    
    public Piece getMovedPiece(){
        return this.movedPiece;
    }
    
    public boolean isAttack(){
        return false;
    }
    
    public boolean isCastlingMove(){
        return false;
    }
    
    public Piece getAttackedPiece(){
        return null;
    }
    
    public boolean attackSuccess(Piece aPiece, Piece dPiece){
        int roll = roll();
        String attPiece = aPiece.toString();
        String defPiece = dPiece.toString();
        System.out.println("You rolled a " + roll +".");
        
        if (attPiece.equals("P")){ //attacking piece is a pawn
            if (defPiece.equals("P")){
                if (roll >= 4){
                    return true;
                }else{
                    return false;
                }
            }else if(defPiece.equals("N")){
                if (roll >= 5){
                    return true;
                }else{
                    return false;                    
                }
            }else{
                if (roll >= 6){
                    return true;
                }else{
                    return false;
                }
            }
        }else if (attPiece.equals("N")){//attacking piece is a knight
            if (defPiece.equals("P")){
                if (roll >= 3){
                    return true;
                }else{
                    return false;
                }
            }else if (defPiece.equals("N")){
                if (roll >= 4){
                    return true;
                }else{
                    return false;
                }
            }else if (defPiece.equals("B") || defPiece.equals("R")){
                if (roll >= 5){
                    return true;
                }else{
                    return false;
                }
            }else{
                if (roll >= 6){
                    return true;
                }else{
                    return false;
                }
            }
        }else if (attPiece.equals("B") || attPiece.equals("R")){ //attack piece is a bishop or rook
            if (defPiece.equals("P")){
                if (roll >= 2){
                    return true;
                }else{
                    return false;
                }
            }else if (defPiece.equals("N")){
                if (roll >= 3){
                    return true;
                }else{
                    return false;
                }
            }else if (defPiece.equals("B") || defPiece.equals("R")){
                if (roll >= 4){
                    return true;
                }else{
                    return false;
                }
            }else{
                if (roll >= 5){
                    return true;
                }else{
                    return false;
                }
            }
        }else{ //attacking piece is a queen or king
            if (defPiece.equals("P")){
                return true;
            }else if (defPiece.equals("N")){
                if (roll >= 2){
                    return true;
                }else{
                    return false;
                }
            }else if (defPiece.equals("B") || defPiece.equals("R")){
                if (roll >= 3){
                    return true;
                }else{
                    return false;
                }
            }else{
                if (roll >= 4){
                    return true;
                }else{
                    return false;
                }
            }
        }
    }
    
    public void doMoveCount(Builder builder, boolean trueMove){
        if (trueMove){
            if (this.board.getMoveCount() == 0){
                builder.setMoveCount(1);
                builder.setMoveMaker(this.board.currentPlayer().getAlliance());//gives turn to current player
            }
            else{
                builder.setMoveCount(0);
                builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());//gives turn to enemy 
            }
        }else{
            builder.setMoveCount(this.board.getMoveCount());
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        }
    }
    
    public int roll(){
        return (int)(Math.random()*6) + 1;
    }
    
    public Board execute(boolean trueMove) {     
        final Builder builder = new Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        if (isAttack() && trueMove){ //AI will consider each move a success and skip this step
            
            if (!attackSuccess(getMovedPiece(), getAttackedPiece())) //if the attack fails
            {
                builder.setPiece(this.movedPiece); //the moved piece stays in place
                System.out.println("Failed attack.");
            }else{
                builder.setPiece(this.movedPiece.movePiece(this)); //moves the moved piece
                System.out.println("Successful attack.");
            }
        }else{
            builder.setPiece(this.movedPiece.movePiece(this)); //moves the moved piece
        }
        
        doMoveCount(builder, trueMove); //increments move counter
        return builder.build();
    }
    
    public static class MajorAttackMove extends AttackMove{
        
        public MajorAttackMove(final Board board, 
                                final Piece pieceMoved, 
                                final int destinationCoordinate, 
                                final Piece pieceAttacked){
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }
        
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
        
    }
    
    public static final class MajorMove extends Move{
    
        public MajorMove(final Board board, 
                final Piece movedPiece, 
                final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }  
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }
        
        @Override 
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    public static class AttackMove extends Move{
        
        final Piece attackedPiece;
        
        public AttackMove(final Board board, 
                final Piece movedPiece, 
                final int destinationCoordinate, 
                final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }  
        
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        
        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
        
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + "x"+ BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
        
        @Override
        public boolean isAttack(){
            return true;
        }
        
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
    }
    
    public static final class PawnMove extends Move{
    
        public PawnMove(final Board board, 
                final Piece movedPiece, 
                final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }  
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }
        
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate,
                final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
        }
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }
        
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    public static class PawnEnPassantAttackMove extends PawnAttackMove {

        public PawnEnPassantAttackMove(final Board board,
                                final Piece movedPiece,
                                final int destinationCoordinate,
                                final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }
        
        @Override
        public Board execute(boolean trueMove){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!piece.equals(this.getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }
            
            if (!attackSuccess(getMovedPiece(), getAttackedPiece())) //if the attack fails
            {
                builder.setPiece(getAttackedPiece()); //put the attack piece into the builder
                builder.setPiece(this.movedPiece); //the moved piece stays in place
                System.out.println("Successful attack.");
            }else{
                builder.setPiece(this.movedPiece.movePiece(this)); //moves the moved piece
                System.out.println("Failed attack.");
            }
            doMoveCount(builder, trueMove); //increments move counter
            
            return builder.build();
        }
    }
    
    public static final class PawnJump extends Move{
    
        public PawnJump(final Board board, 
                        final Piece movedPiece, 
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }   
        
        @Override
        public Board execute(boolean trueMove){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            doMoveCount(builder, trueMove); //increments move counter
            
            return builder.build();
        }
        
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    static abstract class CastleMove extends Move{
    
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;
        
        public CastleMove(final Board board, 
                        final Piece movedPiece, 
                        final int destinationCoordinate,
                        final Rook castleRook,
                        final int castleRookStart,
                        final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }    
        
        public Rook getCastleRook(){
            return this.castleRook;
        }
        
        @Override
        public boolean isCastlingMove(){
            return true;
        }
        
        @Override
        public Board execute(boolean trueMove){    
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
            //todo look into the first move on normal piece
            doMoveCount(builder, trueMove); //increments move counter
            
            return builder.build();
        }
        
        @Override
        public int hashCode(){
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }
        
        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof CastleMove)){
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove)other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }
    
    public static final class KingSideCastleMove extends CastleMove{
    
        public KingSideCastleMove(final Board board, 
                                  final Piece movedPiece, 
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }  
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof KingSideCastleMove && super.equals(other);           
        }
        
        @Override
        public String toString(){
            return "0-0";
        }
    }
    
    public static final class QueenSideCastleMove extends CastleMove{
    
        public QueenSideCastleMove(final Board board, 
                        final Piece movedPiece, 
                        final int destinationCoordinate,
                        final Rook castleRook,
                        final int castleRookStart,
                        final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        } 
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);           
        }
        
        @Override
        public String toString(){
            return "0-0-0";
        }
    }
    
    public static final class NullMove extends Move{
    
        public NullMove() {
            super(null, 65);
        }  
        
        @Override
        public Board execute(boolean trueMove){
            throw new RuntimeException("cannot execute the null move!");
        }
        
        @Override
        public int getCurrentCoordinate(){
            return -1;
        }
    }
    
    public static class MoveFactory {
        
        private MoveFactory() {
            throw new RuntimeException("Not Instantiable!");
        }
        
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){           
            for(final Move move : board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate &&
                    move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
    
}
