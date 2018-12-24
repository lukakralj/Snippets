package ovalButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class enables background tiling of images.
 *
 * @author Luka Kralj
 * @version 23 December 2018
 */
public class TiledImagePanel extends JPanel {

    private BufferedImage image;

    /**
     * Create new panel.
     *
     * @param image Image that we want for the background.
     */
    public TiledImagePanel(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int imageW = image.getWidth(this);
        int imageH = image.getHeight(this);

        // Tile the image to fill all area.
        for (int x = 0; x < getWidth(); x += imageW) {
            for (int y = 0; y < getHeight(); y += imageH) {
                g.drawImage(image, x, y, null);
            }
        }
    }
}
