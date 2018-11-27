package gui;

import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

public class GameRollPanel extends JPanel{
    
    private final JPanel northPanel = new JPanel();
    private final JLabel northLabel1;
    private final JLabel northLabel2;
    public final JLabel centerPanel;
    private final JLabel moveDisplay;
    
    public GameRollPanel(){
        this.setLayout(new FlowLayout());
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        northPanel.setLayout(new BorderLayout());
        northPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
        northLabel1 = new JLabel("WHITE's");
        northLabel1.setFont(new Font("Calibri", Font.ITALIC, 16));
        northLabel1.setBackground(Color.BLACK);
        northLabel2 = new JLabel("first move");
        moveDisplay = new JLabel("");
        northPanel.add(this.northLabel1, BorderLayout.NORTH);
        northPanel.add(this.northLabel2, BorderLayout.CENTER);
        northPanel.add(this.moveDisplay, BorderLayout.SOUTH);
        
        centerPanel = new JLabel(new ImageIcon(getClass().getResource("/images/misc/die1.png")));
        centerPanel.setVisible(false);
        changeDie(1);

        this.add(this.northPanel);
        this.add(this.centerPanel);
        setPreferredSize(new Dimension(100, 200));
    }
    public void setMoveText(String moveLog){
        moveDisplay.setText(moveLog);
    }
    
    public void turn(int num, String currentPlayer){
        if (num == 0)
            northLabel2.setText("   first move   ");
        else
            northLabel2.setText("second move");
        if (currentPlayer.equals("White"))
        {
            northLabel1.setForeground(Color.BLACK);
            northLabel1.setOpaque(false);
            northLabel1.setText("WHITE's");
        }
        else
        {
            northLabel1.setForeground(Color.WHITE);
            northLabel1.setOpaque(true);
            northLabel1.setText("BLACK's");
        }
    }
    
    public void changeDie(int roll){
        switch(roll)
        {
            case 1:
                centerPanel.setIcon(new ImageIcon(getClass().getResource("/images/misc/die1.png")));
                break;
            case 2:
                centerPanel.setIcon(new ImageIcon(getClass().getResource("/images/misc/die2.png")));
                break;
            case 3:
                centerPanel.setIcon(new ImageIcon(getClass().getResource("/images/misc/die3.png")));
                break;
            case 4:
                centerPanel.setIcon(new ImageIcon(getClass().getResource("/images/misc/die4.png")));
                break;
            case 5:
                centerPanel.setIcon(new ImageIcon(getClass().getResource("/images/misc/die5.png")));
                break;
            case 6:
                centerPanel.setIcon(new ImageIcon(getClass().getResource("/images/misc/die6.png")));
                break;
        }
    }
}