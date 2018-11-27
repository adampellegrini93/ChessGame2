package gui;

import board.Board;
import board.BoardUtils;
import board.Move;
import board.Move.MoveFactory;
import board.Tile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import static javax.swing.SwingUtilities.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import pieces.Piece;
import player.MoveTransition;
import player.ai.MiniMax;
import player.ai.MoveStrategy;

public class Table extends Observable{
    
    private final JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final GameRollPanel gameRollPanel;
    private final BoardPanel boardPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final MoveLog moveLog;
    private final GameSetup gameSetup;
    private final InfoOutput info;
    private Board chessBoard;
    private Move computerMove;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;
    
    private final  Dimension OUTER_FRAME_DIMENSION = new Dimension(900,700);
    private final  Dimension BOARD_PANEL_DIMENSION = new Dimension(500,450);
    private final  Dimension TILE_PANEL_DIMENSION = new Dimension(20,20);
    
    private final Color lightTileColor = Color.ORANGE;
    private final Color darkTileColor = Color.lightGray;
    
    private static final Table INSTANCE = new Table();
    
    public Table(){
        this.gameFrame = new JFrame("Fuzzy Logic Chess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.gameRollPanel = new GameRollPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.info = new InfoOutput();
        this.highlightLegalMoves = true;
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.takenPiecesPanel,BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.add(this.gameRollPanel, BorderLayout.EAST);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setVisible(true);
        
        //stops program from continuosly running after main window is closed
        this.gameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
            System.exit(0);
            }
        });
    }
    
    public static Table get(){
        return INSTANCE;
    }
    
    public void show(){
        gameFrame.setVisible(true);
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().moveLog);
        Table.get().getGameRollPanel().turn(Table.get().getGameBoard().getMoveCount(), Table.get().getGameBoard().currentPlayer().toString());
        Table.get().getGameRollPanel().changeDie(Table.get().getGameBoard().getLastRoll());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }
    
    public void hide(){
        gameFrame.setVisible(false);
    }
    
    public GameSetup getGameSetup(){
        return this.gameSetup;
    }
    
    private Board getGameBoard(){
        return this.chessBoard;
    }
    
    private JFrame getGameFrame(){
        return this.gameFrame;
    }
    
    //creates and loads all of the menu bars
    private JMenuBar createTableMenuBar(){
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }

    //creates and populates the File drop down menu
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Show game info");
        openPGN.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                    info.setVisible(true);
            }
        });
        fileMenu.add(openPGN);
        fileMenu.addSeparator();
        
        final JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Table.get().hide(); //NOTE: entire program ends when any exit button is pressed. 
                                    //Hiding the main pane gives the prevents players from "cancelling" making a new game
                Table.get().getGameSetup().update(false, true, false, true, 1, 1);  //force ends ai
                Table.get().updateGameBoard(Board.createStandardBoard());
                StartScreen start = new StartScreen();
                start.setVisible(true);
            }
        });
        fileMenu.add(newGameMenuItem);
        fileMenu.addSeparator();
        
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Table.get().getGameFrame().dispose();
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        
        return fileMenu;
    }
    
    //creates and populates the preferences drop down menu
    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        
        preferencesMenu.addSeparator();
        
        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight legal moves",true);
        legalMoveHighlighterCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckbox);
        return preferencesMenu;
    }
    
    private JMenu createOptionsMenu(){
        final JMenu optionsMenu = new JMenu("Options");
        
        final JMenuItem setupGameMenuItem = new JMenuItem("Settings");
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //Table.get().getGameSetup().promptUser();
                //Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });
        
        optionsMenu.add(setupGameMenuItem);
        
        return optionsMenu;
    }
    
    public void setupUpdate(final GameSetup gameSetup){
        setChanged();
        notifyObservers(gameSetup);
    }
    
    private static class TableGameAIWatcher
            implements Observer{

        @Override
        public void update(final Observable o,final Object o1) {
            
            if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer())) {
                //create an AI thread and execute it
                final AIThinkTank thinkTank = new AIThinkTank();
                if(!Table.get().getGameBoard().currentPlayer().kingTaken() && !Table.get().getGameBoard().currentPlayer().getOpponent().kingTaken()) //prevents ai from continuing when game ends
                    thinkTank.execute();
            }
            
            if(Table.get().getGameBoard().currentPlayer().kingTaken()){
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(), "Game Over, "+ Table.get().getGameBoard().currentPlayer() + "'s king was taken!", 
                        "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
            else if (Table.get().getGameBoard().currentPlayer().getOpponent().kingTaken()){
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(), "Game Over, "+ Table.get().getGameBoard().currentPlayer().getOpponent() + "'s king was taken!", 
                        "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        }     
    }
    
    public void updateGameBoard(final Board board){
        this.chessBoard = board;
    }
    
    public void updateComputerMove(final Move move){
        this.computerMove = move;
    }
    
    private MoveLog getMoveLog(){
        return this.moveLog;
    }
    
    private GameHistoryPanel getGameHistoryPanel(){
        return this.gameHistoryPanel;
    }
    
    private GameRollPanel getGameRollPanel(){
        return this.gameRollPanel;
    }
    
    private TakenPiecesPanel getTakenPiecesPanel(){
        return this.takenPiecesPanel;
    }
    
    private BoardPanel getBoardPanel(){
        return this.boardPanel;
    }
    
    private void moveMadeUpdate(final PlayerType playerType){
        setChanged();
        notifyObservers(playerType);
    }
    
    //swingworker allows threadwork in gui without blocking gui thread
    private static class AIThinkTank extends SwingWorker<Move, String>{
        
        private AIThinkTank(){
            
        }
        
        @Override
        protected Move doInBackground() throws Exception{
            final MoveStrategy miniMax = new MiniMax(Table.get().gameSetup.getSearchDepth(Table.get().getGameBoard().currentPlayer().getAlliance()));
            final Move bestMove = miniMax.execute(Table.get().getGameBoard()); 
            return bestMove;
        }
        
        @Override
        public void done(){
            
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove, true).getTransitionBoard());
                if(Table.get().getGameBoard().getMoveCount() == 1){
                    System.out.println(Table.get().getGameBoard().currentPlayer().toString() + " player has made their first move!");
                }
                else{
                    System.out.println(Table.get().getGameBoard().currentPlayer().getOpponent().toString() + " player has made their second move!");
                }
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getGameRollPanel().turn(Table.get().getGameBoard().getMoveCount(), Table.get().getGameBoard().currentPlayer().toString());
                Table.get().getGameRollPanel().changeDie(Table.get().getGameBoard().getLastRoll());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    //used for changing the board direction 
    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles){
                return boardTiles;
            }
            
            @Override
            BoardDirection opposite(){
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles){
                Collections.reverse(boardTiles);
                return boardTiles;
            }
            
            @Override
            BoardDirection opposite(){
                return NORMAL;
            }
        };
        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }
    
    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i < BoardUtils.NUM_TILES;i++){
                final TilePanel tilePanel = new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
        
        public void drawBoard(final Board board){
            removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }
    
    public static class MoveLog{
        private final List<Move> moves;
        
        MoveLog(){
            this.moves = new ArrayList<>();
        }
        
        public List<Move> getMoves(){
            return this.moves;
        }
        
        public void addMove(final Move move){
            this.moves.add(move);
        }
        
        public int size(){
            return this.moves.size();
        }
        
        public void clear(){
            this.moves.clear();
        }
        
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }
    }
    
    enum PlayerType {
        HUMAN,
        COMPUTER
    }
    
    private class TilePanel extends JPanel{
        
        private final int tileId;
        
        TilePanel(final BoardPanel boardPanel, final int tileID){
            super(new GridBagLayout());
            this.tileId = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent me) {
                    
                    if(isRightMouseButton(me)){                       
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;                       
                    }else if(isLeftMouseButton(me) && 
                            !gameSetup.isAIPlayer(chessBoard.currentPlayer()) && 
                            !chessBoard.currentPlayer().kingTaken() && 
                            !chessBoard.currentPlayer().getOpponent().kingTaken()){
                        if(sourceTile == null){
                            sourceTile = chessBoard.getTile(tileID);
                            humanMovedPiece = sourceTile.getPiece();
                            if(humanMovedPiece == null){
                                sourceTile = null;
                            }
                        }else{
                            destinationTile = chessBoard.getTile(tileID);
                            final Move move = MoveFactory.createMove(chessBoard, 
                                    sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move, true);
                            if(transition.getMoveStatus().isDone()){
                                if(info.isVisible()){
                                    info.addText(chessBoard.currentPlayer().toString()+" player has made their move!");
                                    info.addText("It is now " + chessBoard.currentPlayer().getOpponent().toString()+ " playes turn!");
                                }
                                chessBoard = transition.getTransitionBoard();
                                Table.get().getGameRollPanel().turn(Table.get().getGameBoard().getMoveCount(), Table.get().getGameBoard().currentPlayer().toString());
                                Table.get().getGameRollPanel().changeDie(Table.get().getGameBoard().getLastRoll());
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                gameHistoryPanel.redo(chessBoard, moveLog);
                                takenPiecesPanel.redo(moveLog);
                                
                                boardPanel.drawBoard(chessBoard);
                                
                                if(gameSetup.isAIPlayer(chessBoard.currentPlayer())){
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                else{
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                            }
                        });
                    }                                         
                }

                @Override
                public void mousePressed(final MouseEvent me) {
                    
                }

                @Override
                public void mouseReleased(final MouseEvent me) {
                    
                }

                @Override
                public void mouseEntered(final MouseEvent me) {
                    
                }

                @Override
                public void mouseExited(final MouseEvent me) {
                    
                }
            });   
            validate();
        }
        
        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }
        
        //sets chess piece images
        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
                if(board.getTile(this.tileId).getPiece() == humanMovedPiece && humanMovedPiece != null && 
                        humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()){
                String pieceImage2 = humanMovedPiece.getPieceAlliance().toString().substring(0,1)
                                    + humanMovedPiece.toString().toUpperCase() + ".png";
                                JLabel label11 = new JLabel(new ImageIcon(getClass().getResource("/images/pieces/" + pieceImage2)));
                                label11.setLayout(new BorderLayout());
                                add(label11);
                                JLabel label22 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/square.png")));
                                label22.setHorizontalAlignment(JLabel.CENTER);
                                label11.add(label22);
                                setVisible(true);
                }else{ 
                    String pieceImage = board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)
                            + board.getTile(this.tileId).toString().toUpperCase() + ".png";
                    JLabel label = new JLabel(new ImageIcon(getClass().getResource("/images/pieces/" + pieceImage)));
                    add(label);
                }
            }
        }
        
        //highlight available moves for selected piece
        private void highlightLegals(final Board board){
            if(highlightLegalMoves){
                for(final Move move : pieceLegalMoves(board)){
                    if(move.getDestinationCoordinate() == this.tileId ){
                        if(move.isAttack()){
                            removeAll();
                            if(board.getTile(this.tileId).getPiece() != null){
                                String pieceImage = board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)
                                    + board.getTile(this.tileId).toString().toUpperCase() + ".png";
                                
                                JLabel label1 = new JLabel(new ImageIcon(getClass().getResource("/images/pieces/" + pieceImage)));
                                label1.setLayout(new BorderLayout());
                                label1.setHorizontalAlignment(JLabel.CENTER);
                                add(label1);
                                
                                JLabel label2 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/redX_50x50.png")));
                                label2.setLayout(new BorderLayout());
                                label1.add(label2, BorderLayout.CENTER);
                                
                                JLabel label3 = new JLabel();
                                if (board.getTile(this.tileId).getPiece().toString().equals("P")) //defeding piece is a pawn
                                    if (move.getMovedPiece().toString().equals("P"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die4_sm.png")));
                                    else if (move.getMovedPiece().toString().equals("N"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die3_sm.png")));
                                    else if (move.getMovedPiece().toString().equals("B") || move.getMovedPiece().toString().equals("R"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die2_sm.png")));
                                    else
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die1_sm.png")));
                                
                                else if (board.getTile(this.tileId).getPiece().toString().equals("N")) //defending piece is a knight
                                    if (move.getMovedPiece().toString().equals("P"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die5_sm.png")));
                                    else if (move.getMovedPiece().toString().equals("N"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die4_sm.png")));
                                    else if (move.getMovedPiece().toString().equals("B") || move.getMovedPiece().toString().equals("R"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die3_sm.png")));
                                    else
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die2_sm.png")));
                                
                                else if (board.getTile(this.tileId).getPiece().toString().equals("B") || board.getTile(this.tileId).getPiece().toString().equals("R")) //defending piece is a bishop or rook
                                    if (move.getMovedPiece().toString().equals("P"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die6_sm.png")));
                                    else if (move.getMovedPiece().toString().equals("N"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die5_sm.png")));
                                    else if (move.getMovedPiece().toString().equals("B") || move.getMovedPiece().toString().equals("R"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die4_sm.png")));
                                    else
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die3_sm.png")));
                                else //defeding piece is a king or queen
                                    if (move.getMovedPiece().toString().equals("P") || move.getMovedPiece().toString().equals("N"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die6_sm.png")));
                                    else if (move.getMovedPiece().toString().equals("B") || move.getMovedPiece().toString().equals("R"))
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die5_sm.png")));
                                    else
                                        label3 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die4_sm.png")));
                                    
                                label3.setHorizontalAlignment(JLabel.RIGHT);
                                label3.setVerticalAlignment(JLabel.BOTTOM);
                                label2.add(label3, BorderLayout.LINE_END);
                                
                                setVisible(true);
                            }else{
                                JLabel label2 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/redX_50x50.png")));
                                label2.setHorizontalAlignment(JLabel.CENTER);
                                add(label2);
                                setVisible(true);
                            }
                        }else{
                            add(new JLabel(new ImageIcon(getClass().getResource("/images/misc/check.png"))));    
                        }
                    }
                }
            }
        }
        
        private Collection<Move> pieceLegalMoves(final Board board){
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()){
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
        
        //creates board layout design
        private void assignTileColor() {
            if(BoardUtils.EIGHTH_RANK[this.tileId] ||
                    BoardUtils.SIXTH_RANK[this.tileId] ||
                    BoardUtils.FOURTH_RANK[this.tileId] ||
                    BoardUtils.SECOND_RANK[this.tileId]){
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            }else if(BoardUtils.SEVENTH_RANK[this.tileId] ||
                    BoardUtils.FIFTH_RANK[this.tileId] ||
                    BoardUtils.THIRD_RANK[this.tileId] ||
                    BoardUtils.FIRST_RANK[this.tileId]){
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }
    }
}
