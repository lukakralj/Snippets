package ovalButton.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import ovalButton.TiledImagePanel;
import ovalButton.OvalButton;

/**
 * An example of how to use TiledImagePanel and OvalButton classes.
 *
 * @author Luka Kralj
 * @version 23 December 2018
 */
public class Example {

    private JFrame frame;

    public Example() {
        frame = new JFrame("Example - oval and capsule shaped buttons");

        frame.setPreferredSize(getRelativeSize(1,0.5));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUp();

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Produce dimension that depends on the screen size.
     *
     * @param widthPercent 0-1, where 1 means screen width.
     * @param heightPercent 0-1, where 1 means screen height.
     * @return Absolute dimension.
     */
    private Dimension getRelativeSize(double widthPercent, double heightPercent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int)(screenSize.width * widthPercent), (int)(screenSize.height * heightPercent));
    }

    /**
     * Set up all the buttons and the background panel.
     */
    private void setUp() {
        JPanel outer = new TiledImagePanel(loadBackgroundImage());
        outer.setLayout(new BorderLayout());
        JLabel label = new JLabel("<html><font size='5' color='white'>Try clicking all the buttons and observe their behaviour. Try clicking around their borders too!</font></html>");
        outer.add(label, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.setOpaque(false);

        // oval, color
        OvalButton button1 = new OvalButton();
        button1.setText("<html><font size='4'>Click me!</font><br><br>shape: oval,<br>background: color</html>");
        button1.setPreferredSize(getRelativeSize(0.15, 0.3));
        button1.addActionListener(e -> buttonClicked(button1));
        mainPanel.add(button1);

        // oval, image
        OvalButton button2 = new ImageOvalButton(OvalButton.SHAPE_OVAL, OvalButton.VERTICAL);
        button2.setText("<html><font size='4'>Click me!</font><br><br>shape: oval,<br>background: image</html>");
        button2.setPreferredSize(getRelativeSize(0.15, 0.3));
        button2.addActionListener(e -> buttonClicked(button2));
        mainPanel.add(button2);

        // capsule, color, vertical
        OvalButton button3 = new OvalButton(OvalButton.SHAPE_CAPSULE, OvalButton.VERTICAL);
        button3.setText("<html><font size='4'>Click me!</font><br><br>shape: capsule,<br>background: color,<br>orientation: vertical</html>");
        button3.setPreferredSize(getRelativeSize(0.15, 0.3));
        button3.addActionListener(e -> buttonClicked(button3));
        mainPanel.add(button3);

        // capsule, color, horizontal
        OvalButton button4 = new OvalButton(OvalButton.SHAPE_CAPSULE, OvalButton.HORIZONTAL);
        button4.setText("<html><font size='4'>Click me!</font><br><br>shape: capsule,<br>background: color,<br>orientation: horizontal</html>");
        button4.setPreferredSize(getRelativeSize(0.15, 0.3));
        button4.addActionListener(e -> buttonClicked(button4));
        mainPanel.add(button4);

        // capsule, image, vertical
        OvalButton button5 = new ImageOvalButton(OvalButton.SHAPE_CAPSULE, OvalButton.VERTICAL);
        button5.setText("<html><font size='4'>Click me!</font><br><br>shape: capsule,<br>background: image,<br>orientation: vertical</html>");
        button5.setPreferredSize(getRelativeSize(0.15, 0.3));
        button5.addActionListener(e -> buttonClicked(button5));
        mainPanel.add(button5);

        // capsule, image, horizontal
        OvalButton button6 = new ImageOvalButton(OvalButton.SHAPE_CAPSULE, OvalButton.HORIZONTAL);
        button6.setText("<html><font size='4'>Click me!</font><br><br>shape: capsule,<br>background: image,<br>orientation: horizontal</html>");
        button6.setPreferredSize(getRelativeSize(0.15, 0.3));
        button6.addActionListener(e -> buttonClicked(button6));
        mainPanel.add(button6);

        outer.add(mainPanel, BorderLayout.CENTER);
        frame.getContentPane().add(outer);
    }


    /**
     * Loads the background image.
     */
    private BufferedImage loadBackgroundImage() {
        try {
            return ImageIO.read(new File("ovalButton/example/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Toggle highlighted border.
     *
     * @param buttonClicked Button that was clicked.
     */
    private void buttonClicked(OvalButton buttonClicked) {
        buttonClicked.setHighlightedBorder(!buttonClicked.isBorderHighlighted());
    }

    public static void main(String[] args) {
        new Example();
    }
}
