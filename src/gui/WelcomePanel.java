
package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class WelcomePanel extends JPanel
{

    @Override
    public void paint(Graphics g)
    {
        try {        
       
            String fileName = System.getProperty("user.dir") + 
                File.separator + "src" +
                File.separator + "data" + File.separator + "frog.jpg";
            BufferedImage image = ImageIO.read(new File(fileName));
            g.drawImage(image, 0, 0, null);
        } catch (IOException ex) {
            Logger.getLogger(WelcomePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }  



}
