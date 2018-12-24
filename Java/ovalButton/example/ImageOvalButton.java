package ovalButton.example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import ovalButton.OvalButton;
import javax.imageio.ImageIO;

/**
 * An ovalButton.example class that uses image as the background instead of a simple color.
 *
 * @author Luka Kralj
 * @version 23 December 20018
 */
public class ImageOvalButton extends OvalButton {

    public ImageOvalButton(int shape, int orientation) {
        super(shape,orientation);
    }

    /**
     * This method needs to be overridden to return the image we want to set as the background.
     *
     * @return
     */
    @Override
    protected BufferedImage getBackgroundImage() {
        try {
            return ImageIO.read(new File("ovalButton/example/button_background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
