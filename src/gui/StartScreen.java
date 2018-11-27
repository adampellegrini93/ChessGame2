package gui;

import gui.Table.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import pieces.Alliance;

public class StartScreen extends javax.swing.JDialog {

    private PlayerType whitePlayer;
    private PlayerType blackPlayer;
    private Alliance playerAlliance;
    private Alliance enemyAlliance;
    private int whiteDifficulty;
    private int blackDifficulty;
    
    public StartScreen() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Fuzzy Logic Chess");
        
        //stops program from continuosly running after main window is closed
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
            System.exit(0);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        whiteDifficultyOptions = new javax.swing.ButtonGroup();
        blackPlayerOptions = new javax.swing.ButtonGroup();
        whitePlayerOptions = new javax.swing.ButtonGroup();
        blackDifficultyOptions = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        howToPlayButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        whiteEasyRadioButton = new javax.swing.JRadioButton();
        whiteHardRadioButton = new javax.swing.JRadioButton();
        difficultyLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        whiteHumanRadioButton = new javax.swing.JRadioButton();
        whiteComputerRadioButton = new javax.swing.JRadioButton();
        blackHumanRadioButton = new javax.swing.JRadioButton();
        blackComputerRadioButton = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        blackEasyRadioButton = new javax.swing.JRadioButton();
        blackHardRadioButton = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(420, 450));
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setPreferredSize(new java.awt.Dimension(425, 450));
        setResizable(false);
        setSize(new java.awt.Dimension(425, 450));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Welcome to Fuzzy Logic Chess");

        jButton1.setText("Start Game");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        howToPlayButton.setText("How To Play");
        howToPlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                howToPlayButtonActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Who will play as White?");

        whiteDifficultyOptions.add(whiteEasyRadioButton);
        whiteEasyRadioButton.setSelected(true);
        whiteEasyRadioButton.setText("Easy");
        whiteEasyRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                whiteEasyRadioButtonActionPerformed(evt);
            }
        });

        whiteDifficultyOptions.add(whiteHardRadioButton);
        whiteHardRadioButton.setText("Hard");

        difficultyLabel.setText("Difficulty of white computer?");

        jLabel4.setText("(Ignore if 2 players selected)");

        jLabel3.setText("Who will play as Black?");

        whitePlayerOptions.add(whiteHumanRadioButton);
        whiteHumanRadioButton.setSelected(true);
        whiteHumanRadioButton.setText("Human");

        whitePlayerOptions.add(whiteComputerRadioButton);
        whiteComputerRadioButton.setText("Computer");

        blackPlayerOptions.add(blackHumanRadioButton);
        blackHumanRadioButton.setSelected(true);
        blackHumanRadioButton.setText("Human");

        blackPlayerOptions.add(blackComputerRadioButton);
        blackComputerRadioButton.setText("Computer");

        jLabel6.setText("Difficulty of black computer?");

        blackDifficultyOptions.add(blackEasyRadioButton);
        blackEasyRadioButton.setSelected(true);
        blackEasyRadioButton.setText("Easy");

        blackDifficultyOptions.add(blackHardRadioButton);
        blackHardRadioButton.setText("Hard");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(blackHumanRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blackComputerRadioButton))
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(whiteEasyRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(whiteHardRadioButton))
                            .addComponent(difficultyLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(blackEasyRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blackHardRadioButton))
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(whiteHumanRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(whiteComputerRadioButton)))
                        .addGap(120, 120, 120))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(95, 95, 95))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(howToPlayButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(howToPlayButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(whiteHumanRadioButton)
                    .addComponent(whiteComputerRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blackHumanRadioButton)
                    .addComponent(blackComputerRadioButton))
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(difficultyLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(whiteEasyRadioButton)
                    .addComponent(whiteHardRadioButton))
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blackEasyRadioButton)
                    .addComponent(blackHardRadioButton))
                .addGap(44, 44, 44)
                .addComponent(jButton1)
                .addGap(36, 36, 36))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void whiteEasyRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whiteEasyRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_whiteEasyRadioButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(whiteEasyRadioButton.isSelected()){
            whiteDifficulty = 1;
        }else{
            whiteDifficulty = 2;
        }
        if(blackEasyRadioButton.isSelected()){
            blackDifficulty = 1;
        }else{
            blackDifficulty = 2;
        }
        if(whiteHumanRadioButton.isSelected() && blackHumanRadioButton.isSelected()){
            Table.get().getGameSetup().update(false, true, false, true, whiteDifficulty, blackDifficulty);
            Table.get().setupUpdate(Table.get().getGameSetup());
            Table.get().show();
            this.dispose();
            //this.setVisible(true);
        }
        else if(whiteHumanRadioButton.isSelected() && blackComputerRadioButton.isSelected()){
            Table.get().getGameSetup().update(false, true, true, false, whiteDifficulty, blackDifficulty);
            Table.get().setupUpdate(Table.get().getGameSetup());
            Table.get().show();
            this.dispose();
            //this.setVisible(false);
        }
        else if(whiteComputerRadioButton.isSelected() && blackHumanRadioButton.isSelected()){
            Table.get().getGameSetup().update(true, false, false, true, whiteDifficulty, blackDifficulty);
            Table.get().setupUpdate(Table.get().getGameSetup());
            Table.get().show();
            this.dispose();
            //this.setVisible(false);
        }
        else if(whiteComputerRadioButton.isSelected() && blackComputerRadioButton.isSelected()){
            Table.get().getGameSetup().update(true, false, true, false, whiteDifficulty, blackDifficulty);
            Table.get().setupUpdate(Table.get().getGameSetup());
            Table.get().show();
            this.dispose();
            //this.setVisible(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void howToPlayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_howToPlayButtonActionPerformed
        HowToPlay howTo = new HowToPlay();
        howTo.setVisible(true);
    }//GEN-LAST:event_howToPlayButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StartScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StartScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StartScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StartScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StartScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton blackComputerRadioButton;
    private javax.swing.ButtonGroup blackDifficultyOptions;
    private javax.swing.JRadioButton blackEasyRadioButton;
    private javax.swing.JRadioButton blackHardRadioButton;
    private javax.swing.JRadioButton blackHumanRadioButton;
    private javax.swing.ButtonGroup blackPlayerOptions;
    private javax.swing.JLabel difficultyLabel;
    private javax.swing.JButton howToPlayButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JRadioButton whiteComputerRadioButton;
    private javax.swing.ButtonGroup whiteDifficultyOptions;
    private javax.swing.JRadioButton whiteEasyRadioButton;
    private javax.swing.JRadioButton whiteHardRadioButton;
    private javax.swing.JRadioButton whiteHumanRadioButton;
    private javax.swing.ButtonGroup whitePlayerOptions;
    // End of variables declaration//GEN-END:variables
}
