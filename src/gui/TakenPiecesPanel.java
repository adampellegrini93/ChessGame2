package gui;

import board.Move;
import gui.Table.MoveLog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import pieces.Piece;


public class TakenPiecesPanel extends JPanel{
    
    private final JPanel northPanel;
    private final JPanel southPanel;
    
    private static final Color PANEL_COLOR = Color.LIGHT_GRAY;
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(130,100);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    
    public TakenPiecesPanel(){
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8,2));
        this.southPanel = new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }
    
    public void redo(final MoveLog moveLog){
        this.southPanel.removeAll();
        this.northPanel.removeAll();
        
        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();
        
        for(final Move move : moveLog.getMoves()){
            if(move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceAlliance().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                }else if(takenPiece.getPieceAlliance().isBlack()){
                    blackTakenPieces.add(takenPiece);
                }else{
                    throw new RuntimeException("should not reach here!");
                }
            }
        }
        /*
        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece t, Piece t1) {
                return Integer.valueOf(t.getPieceValue()).compareTo(Integer.valueOf(t1.getPieceValue()));
                //return Ints.compare(t.getPieceValue(),t1.getPieceValue());
            }
        });
        
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece t, Piece t1) {
                return Integer.valueOf(t.getPieceValue()).compareTo(Integer.valueOf(t1.getPieceValue()));
                //return Ints.compare(t.getPieceValue(),t1.getPieceValue());
            }
        });
        */
        for(final Piece takenPiece : whiteTakenPieces){
            String pieceImage = takenPiece.getPieceAlliance().toString().substring(0,1)+""+takenPiece.toString() + ".png";
            ImageIcon image = new ImageIcon(getClass().getResource("/images/pieces/" + pieceImage));
            Image image2 = image.getImage();
            Image newimg = image2.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
            this.southPanel.add(new JLabel(new ImageIcon(newimg)));
        }
        
        for(final Piece takenPiece : blackTakenPieces){
            String pieceImage = takenPiece.getPieceAlliance().toString().substring(0,1)+""+takenPiece.toString() + ".png";
            ImageIcon image = new ImageIcon(getClass().getResource("/images/pieces/" + pieceImage));
            Image image2 = image.getImage();
            Image newimg = image2.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
            this.northPanel.add(new JLabel(new ImageIcon(newimg)));
                //this.northPanel.add(new JLabel(new ImageIcon(getClass().getResource("/images/pieces/" + pieceImage))));
        }
        validate();
    }
    
}
