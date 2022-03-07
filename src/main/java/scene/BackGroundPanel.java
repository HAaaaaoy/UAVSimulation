package scene;

import GUItil.GUItil;
import jdk.jshell.execution.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class BackGroundPanel extends JPanel {

    public final BufferedImage cloudOriginal = ImageIO.read(new FileInputStream("./image/cloud.png"));

    public BackGroundPanel() throws IOException {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBounds(0,0,GUItil.getBounds().width,GUItil.getBounds().height);
        int x = 0, y = 0;
        while (true) {
            g.drawImage(cloudOriginal, x, y, this);
            if (x > GUItil.getBounds().width && y > GUItil.getBounds().height) break;
            if (x > GUItil.getBounds().width) {
                x = 0;
                y += cloudOriginal.getHeight();
            } else {
                x += cloudOriginal.getWidth();
            }
        }
    }


}
