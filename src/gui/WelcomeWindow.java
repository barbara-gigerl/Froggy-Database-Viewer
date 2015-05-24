package gui;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class WelcomeWindow extends JFrame implements Runnable {

    public WelcomeWindow()
    {
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setSize(800, 500);
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 800) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - 500) / 2);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setFront();
            }
        });
        WelcomePanel panWelcome = new WelcomePanel();
        this.getContentPane().add(panWelcome);

        this.setLayout(new GridLayout(1, 1));
        this.setVisible(true);
    }

    private void setFront() {
        this.toFront();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
            this.dispose();
        } catch (InterruptedException ex) {
            Logger.getLogger(WelcomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
