package gui;

import gui.Table.*;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import pieces.Alliance;
import player.Player;


class GameSetup extends JDialog{
    
    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;
    private JSpinner searchDepthSpinner;
    private int whiteDifficulty = 1;
    private int blackDifficulty = 1;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";
    
    private final JRadioButton whiteHumanButton;
    private final JRadioButton whiteComputerButton;
    private final JRadioButton blackHumanButton;
    private final JRadioButton blackComputerButton;

    GameSetup(final JFrame frame,
              final boolean modal) {
        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        this.whiteHumanButton = new JRadioButton(HUMAN_TEXT);
        this.whiteComputerButton = new JRadioButton(COMPUTER_TEXT);
        this.blackHumanButton = new JRadioButton(HUMAN_TEXT);
        this.blackComputerButton = new JRadioButton(COMPUTER_TEXT);
        whiteHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(whiteHumanButton);
        whiteGroup.add(whiteComputerButton);
        whiteHumanButton.setSelected(true);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(blackHumanButton);
        blackGroup.add(blackComputerButton);
        blackHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("White"));
        myPanel.add(whiteHumanButton);
        myPanel.add(whiteComputerButton);
        myPanel.add(new JLabel("Black"));
        myPanel.add(blackHumanButton);
        myPanel.add(blackComputerButton);

        myPanel.add(new JLabel("Search"));
        this.searchDepthSpinner = addLabeledSpinner(myPanel, "Search Depth", new SpinnerNumberModel(6, 0, Integer.MAX_VALUE, 1));
        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whitePlayerType = whiteComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                blackPlayerType = blackComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                GameSetup.this.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(null);
        pack();
        setVisible(false);
    }
    
    public void update(boolean whiteComputer, boolean whiteHuman, boolean blackComputer, boolean blackHuman, int whiteDifficulty, int blackDifficulty){
        if(whiteComputer == true){
            if(blackComputer == true){
                whitePlayerType = PlayerType.COMPUTER;
                blackPlayerType = PlayerType.COMPUTER;
                whiteComputerButton.setSelected(true);
                blackComputerButton.setSelected(true);
            }else if(blackHuman == true){
                whitePlayerType = PlayerType.COMPUTER;
                blackPlayerType = PlayerType.HUMAN;
                whiteComputerButton.setSelected(true);
                blackHumanButton.setSelected(true);
            }
        }
        else if(whiteHuman == true){
            if(blackComputer == true){
                whitePlayerType = PlayerType.HUMAN;
                blackPlayerType = PlayerType.COMPUTER;
                whiteHumanButton.setSelected(true);
                blackComputerButton.setSelected(true);
            }else if(blackHuman == true){
                whitePlayerType = PlayerType.HUMAN;
                blackPlayerType = PlayerType.HUMAN;
                whiteHumanButton.setSelected(true);
                blackHumanButton.setSelected(true);
            }
        }
        this.whiteDifficulty = whiteDifficulty;
        this.blackDifficulty = blackDifficulty;
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }

    boolean isAIPlayer(final Player player) {
        if(player.getAlliance() == Alliance.White) {
            return getWhitePlayerType() == PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == PlayerType.COMPUTER;
    }

    PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }

    private static JSpinner addLabeledSpinner(final Container c,
                                              final String label,
                                              final SpinnerModel model) {
        final JLabel l = new JLabel(label);
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    public int getSearchDepth(Alliance alliance) {
        if(alliance.isWhite()){
            return whiteDifficulty;
        }
        else{
            return blackDifficulty;
        }
    }
}
