
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

public class SplashScreen extends JPanel{
    
    public SplashScreen() {
        JWindow window = new JWindow();
        JLabel label2 = new JLabel(new ImageIcon(getClass().getResource("/images/misc/image.png")));
        window.getContentPane().add((label2), SwingConstants.CENTER);
        window.setSize(900, 700);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
        //Table.get().show();
        StartScreen start = new StartScreen();
        start.setVisible(true);
        window.dispose();
    }
}
